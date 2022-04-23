package com.example.composenews.repository

import com.example.composenews.db.ArticleDao
import com.example.composenews.models.*
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

val DefaultPageSize = 20

interface NewsRepository {
    val interestedTopics: StateFlow<Category>
    val bookmarks: Flow<List<ArticleUI>>
    val interested: Flow<List<ArticleUI>>
    val headlines: Flow<List<ArticleUI>>
    val selectedViewMore: StateFlow<ViewMoreCategory>

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
}

class ComposeNewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher
) : NewsRepository {
    private val _interestedTopics = MutableStateFlow(Category.business)
    override val interestedTopics = _interestedTopics

    private val _selectedViewMore = MutableStateFlow<ViewMoreCategory>(ViewMoreCategory.Headlines)
    override val selectedViewMore: StateFlow<ViewMoreCategory>
        get() = _selectedViewMore

    override val bookmarks: Flow<List<ArticleUI>> = dao.getBookmarks().mapLatest {
        it.map { entity ->
            ArticleUI.fromArticleEntity(entity)
        }
    }

    override val headlines: Flow<List<ArticleUI>> =
        dao.getArticles(ArticleType.headline.name).mapLatest {
            it.map { entity ->
                ArticleUI.fromArticleEntity(entity)
            }
        }.flowOn(defaultDispatcher)

    override val interested: Flow<List<ArticleUI>> =
        dao.getArticles(ArticleType.topic.name).mapLatest {
            it.map { entity ->
                ArticleUI.fromArticleEntity(entity)
            }
        }.flowOn(defaultDispatcher)

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
}