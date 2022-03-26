package com.example.composenews.repository

import com.example.composenews.db.ArticleDao
import com.example.composenews.db.ArticleEntity
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.Category
import com.example.composenews.models.NewsApiResponse
import com.example.composenews.models.SortBy
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


interface ComposeNewsRepository {
    val interestedTopics: Flow<Category>
    suspend fun everything(
        query: String? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): Flow<NewsApiResponse>

    suspend fun getHeadlines(
        category: Category? = null,
        sortBy: SortBy = SortBy.publishedAt
    ): Flow<NewsApiResponse>

    suspend fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    suspend fun bookmarkArticle(article: ArticleUI)
}

class ComposeNewsRepositoryImpl @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher
) : ComposeNewsRepository {

    override val interestedTopics: Flow<Category> =
        flowOf(Category.business, Category.general, Category.sports)

    override suspend fun everything(query: String?, sortBy: SortBy): Flow<NewsApiResponse> {
        return safeApiCall { api.getEverything(query = query, sortBy = sortBy.name) }
    }

    override suspend fun getHeadlines(category: Category?, sortBy: SortBy): Flow<NewsApiResponse> {
        return safeApiCall { api.getTopHeadLines(category = category?.name, sortBy = sortBy.name) }
    }

    private suspend fun safeApiCall(apiCall: suspend () -> NewsApiResponse): Flow<NewsApiResponse> {
        return flow {
            emit(apiCall())
        }.flowOn(defaultDispatcher)
    }

    override suspend fun getBookmarkedArticles() = dao.getArticles()

    override suspend fun bookmarkArticle(article: ArticleUI) = dao.insert(article.toArticleEntity())
}