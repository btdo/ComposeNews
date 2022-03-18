package com.example.composenews.repository

import com.example.composenews.models.NewsApiResponse
import com.example.composenews.models.QueryResult
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


interface ComposeChatRepository {
    suspend fun searchNews(query: String): Flow<QueryResult<NewsApiResponse>>
    suspend fun getHeadlines(): Flow<QueryResult<NewsApiResponse>>
}

class ComposeChatRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher
) : ComposeChatRepository {

    override suspend fun searchNews(query: String): Flow<QueryResult<NewsApiResponse>> {
        return safeApiCall { api.getEverything(query) }
    }

    override suspend fun getHeadlines(): Flow<QueryResult<NewsApiResponse>> {
        return safeApiCall { api.getTopHeadLines() }
    }

    private suspend fun safeApiCall(apiCall: suspend () -> NewsApiResponse): Flow<QueryResult<NewsApiResponse>> {
        return flow {
            val result = apiCall()
            emit(QueryResult.Success(result))
        }.flowOn(defaultDispatcher)
    }
}