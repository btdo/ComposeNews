package com.example.composenews.repository

import com.example.composenews.db.ArticleDao
import com.example.composenews.models.*
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface NewsRepository {
    val interestedTopics: StateFlow<Category>
    val bookmarks: Flow<List<ArticleUI>>
    val interested: Flow<List<ArticleUI>>
    val headlines: Flow<List<ArticleUI>>

    suspend fun refreshNews()

    suspend fun everything(
        query: String? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): NewsApiResponse

    suspend fun getHeadlines(
        category: Category? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): NewsApiResponse

    suspend fun bookmarkArticle(article: ArticleUI)

    suspend fun getArticle(articleId: String): ArticleUI
}

class ComposeNewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher
) : NewsRepository {
    private val _interestedTopics = MutableStateFlow(Category.business)
    override val interestedTopics = _interestedTopics

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
        val headlines = getHeadlines().toList(Category.general, ArticleType.headline)
        val interested =
            getHeadlines(_interestedTopics.value).toList(_interestedTopics.value, ArticleType.topic)
        dao.insert(headlines + interested)
    }

    override suspend fun everything(query: String?, sortBy: SortBy): NewsApiResponse =
        withContext(defaultDispatcher) {
            return@withContext api.getEverything(query = query, sortBy = sortBy.name)
        }

    override suspend fun getHeadlines(category: Category?, sortBy: SortBy): NewsApiResponse =
        withContext(defaultDispatcher) {
            return@withContext api.getTopHeadLines(category = category?.name, sortBy = sortBy.name)
        }

    override suspend fun bookmarkArticle(article: ArticleUI) {
        val articleDb = dao.getArticle(article.id)
        dao.update(articleDb.copy(isBookMarked = !articleDb.isBookMarked))
    }

    override suspend fun getArticle(articleId: String): ArticleUI = withContext(defaultDispatcher) {
        return@withContext ArticleUI.fromArticleEntity(dao.getArticle(articleId))
    }
}