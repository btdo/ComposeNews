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
    val articles: Flow<List<ArticleUI>>
    suspend fun getNewsForHome()

    suspend fun everything(
        query: String? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): NewsApiResponse

    suspend fun getHeadlines(
        category: Category? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): NewsApiResponse

    suspend fun bookmarkArticle(article: ArticleUI)
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

    override val articles: Flow<List<ArticleUI>> = dao.getArticles().mapLatest {
        it.map { entity ->
            ArticleUI.fromArticleEntity(entity)
        }
    }.flowOn(defaultDispatcher)

    override suspend fun getNewsForHome() = withContext(defaultDispatcher) {
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
        dao.update(article.copy(isBookMarked = !article.isBookMarked).toArticleEntity())
    }
}