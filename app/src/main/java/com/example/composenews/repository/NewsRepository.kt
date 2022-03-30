package com.example.composenews.repository

import com.example.composenews.db.ArticleDao
import com.example.composenews.models.*
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface NewsRepository {
    val interestedTopics: Flow<Category>
    val bookmarks: Flow<List<ArticleUI>>
    val headlines: Flow<List<ArticleUI>>
    val popular: Flow<List<ArticleUI>>

    suspend fun everything(
        query: String? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): List<ArticleUI>

    suspend fun getHeadlines(
        category: Category? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): List<ArticleUI>

    suspend fun getBookmarkedArticles(): Flow<List<ArticleUI>>

    suspend fun bookmarkArticle(article: ArticleUI)
}

class ComposeNewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher
) : NewsRepository {
    override val interestedTopics: Flow<Category> =
        flowOf(Category.business)

    override val bookmarks: Flow<List<ArticleUI>>
        get() = dao.getArticles().mapLatest {
            it.map { entity ->
                ArticleUI.fromArticleEntity(entity)
            }
        }

    override val headlines = flow {
        emit(getHeadlines())
    }

    override val popular = interestedTopics.mapLatest {
        getHeadlines(it, sortBy = SortBy.publishedAt)
    }

    override suspend fun everything(query: String?, sortBy: SortBy): List<ArticleUI> =
        withContext(defaultDispatcher) {
            return@withContext api.getEverything(query = query, sortBy = sortBy.name).toList()
        }

    override suspend fun getHeadlines(category: Category?, sortBy: SortBy): List<ArticleUI> =
        withContext(defaultDispatcher) {
            return@withContext api.getTopHeadLines(category = category?.name, sortBy = sortBy.name)
                .toList()
        }

    override suspend fun getBookmarkedArticles(): Flow<List<ArticleUI>> =
        dao.getArticles().mapLatest {
            val articles = it.map { entity ->
                ArticleUI.fromArticleEntity(entity)
            }

            return@mapLatest articles
        }

    override suspend fun bookmarkArticle(article: ArticleUI) {
        if (article.isBookMarked) {
            dao.delete(article = article.toArticleEntity())
        } else {
            dao.insert(article.toArticleEntity())
        }
    }
}