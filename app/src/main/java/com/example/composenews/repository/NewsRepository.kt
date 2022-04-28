package com.example.composenews.repository

import com.example.composenews.db.ArticleDao
import com.example.composenews.db.ArticleEntity
import com.example.composenews.models.*
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val DefaultPageSize = 20

interface NewsRepository {
    val interestedTopics: StateFlow<Category>
    val bookmarks: Flow<List<ArticleEntity>>
    val interested: Flow<List<ArticleEntity>>
    val headlines: Flow<List<ArticleEntity>>
    val selectedViewMore: StateFlow<ViewMoreCategory>
    val viewMoreList: StateFlow<List<ArticleEntity>>

    suspend fun refreshNews()

    suspend fun everything(
        query: String? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): NewsApiResponse

    suspend fun getHeadlines(
        category: Category = Category.general,
        sortBy: SortBy = SortBy.publishedAt,
        pageSize: Int? = DefaultPageSize,
        page: Int? = 1
    ): NewsApiResponse

    suspend fun loadHeadlines(
        category: Category = Category.general,
        pageSize: Int? = DefaultPageSize,
        page: Int? = 1, articleType: ArticleType = ArticleType.headline
    )

    suspend fun bookmarkArticle(article: ArticleUI)

    suspend fun getArticle(articleId: String): ArticleUI

    suspend fun onViewMoreSelected(selected: ViewMoreCategory)

    suspend fun loadMore()
}

class ComposeNewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())
) : NewsRepository {
    private val _interestedTopics = MutableStateFlow(Category.business)
    override val interestedTopics = _interestedTopics

    private val _selectedViewMore = MutableStateFlow<ViewMoreCategory>(ViewMoreCategory.Headlines)
    override val selectedViewMore: StateFlow<ViewMoreCategory>
        get() = _selectedViewMore

    override val bookmarks: Flow<List<ArticleEntity>> = dao.getBookmarks()

    override val headlines: Flow<List<ArticleEntity>> = dao.getArticles(ArticleType.headline.name)

    override val interested: Flow<List<ArticleEntity>> = dao.getArticles(ArticleType.topic.name)

    override val viewMoreList: StateFlow<List<ArticleEntity>> = _selectedViewMore.flatMapLatest {
        val flow = when (it) {
            ViewMoreCategory.Headlines -> headlines
            ViewMoreCategory.Topics -> interested
            ViewMoreCategory.Bookmarks -> bookmarks
        }
        return@flatMapLatest flow
    }.stateIn(scope, SharingStarted.Eagerly, listOf())

    override suspend fun refreshNews() = withContext(defaultDispatcher) {
        dao.deleteAllExceptBookmarks()
        loadHeadlines(Category.general)
        loadHeadlines(category = _interestedTopics.value, articleType = ArticleType.topic)
    }

    override suspend fun loadHeadlines(
        category: Category,
        pageSize: Int?,
        page: Int?, articleType: ArticleType
    ) {
        val headlines = getHeadlines(
            category = category,
            pageSize = pageSize,
            page = page
        ).toList(category = category, articleType)
        dao.insert(headlines)
    }

    override suspend fun everything(query: String?, sortBy: SortBy): NewsApiResponse =
        withContext(defaultDispatcher) {
            return@withContext api.getEverything(query = query, sortBy = sortBy.name)
        }

    override suspend fun getHeadlines(
        category: Category,
        sortBy: SortBy,
        pageSize: Int?,
        page: Int?
    ): NewsApiResponse =
        withContext(defaultDispatcher) {
            return@withContext api.getTopHeadLines(
                category = category.name,
                sortBy = sortBy.name,
                pageSize = pageSize,
                page = page
            )
        }

    override suspend fun bookmarkArticle(article: ArticleUI) {
        val articleDb = dao.getArticle(article.id)
        dao.update(articleDb.copy(isBookMarked = !articleDb.isBookMarked))
    }

    override suspend fun getArticle(articleId: String): ArticleUI = withContext(defaultDispatcher) {
        return@withContext ArticleUI.fromArticleEntity(dao.getArticle(articleId))
    }

    override suspend fun onViewMoreSelected(selected: ViewMoreCategory) {
        _selectedViewMore.value = selected
    }

    override suspend fun loadMore() {
        when (selectedViewMore.value) {
            ViewMoreCategory.Headlines -> loadHeadlines(
                Category.general,
                page = getNextPage(),
                articleType = ArticleType.headline
            )
            ViewMoreCategory.Topics -> loadHeadlines(
                interestedTopics.value,
                page = getNextPage(),
                articleType = ArticleType.topic
            )
        }
    }

    private fun getNextPage(): Int {
        return (viewMoreList.value.size / DefaultPageSize) + 1
    }
}