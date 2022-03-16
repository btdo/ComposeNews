package com.example.composenews.repository

import com.example.composenews.models.NetworkResult
import com.example.composenews.models.NewsApiResponse
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


interface ComposeChatRepository {
    suspend fun searchNews(query: String): Flow<NetworkResult<NewsApiResponse>>
    suspend fun getHeadlines(): Flow<NetworkResult<NewsApiResponse>>
}

class ComposeChatRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val defaultDispatcher: CoroutineDispatcher
) : ComposeChatRepository {

    override suspend fun searchNews(query: String): Flow<NetworkResult<NewsApiResponse>> {
        return safeApiCall { api.getEverything(query) }
    }

    override suspend fun getHeadlines(): Flow<NetworkResult<NewsApiResponse>> {
        return safeApiCall { api.getTopHeadLines() }
    }

    private suspend fun safeApiCall(apiCall: suspend () -> NewsApiResponse): Flow<NetworkResult<NewsApiResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val result = apiCall()
                emit(NetworkResult.Success(result) )
            } catch (e: Exception) {
                emit(NetworkResult.Error(e))
            }
        }.flowOn(defaultDispatcher)
    }
}