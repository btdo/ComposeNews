package com.example.composenews.repository

import android.util.Log
import com.example.composenews.models.NewsApiResponse
import com.example.composenews.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


interface ComposeChatRepository {
    suspend fun getEverything(query: String): NewsApiResponse
    suspend fun getHeadlines(): Flow<NewsApiResponse>
}

class ComposeChatRepositoryImpl @Inject constructor(private val api: NewsApi): ComposeChatRepository {
    override suspend fun getEverything(query: String): NewsApiResponse  {
        try {
            return api.getEverything(query)
        } catch (e: Exception){
            Log.e("ComposeChatRepos", e.message, e)
            return NewsApiResponse(listOf(), "Error",0)
        }
    }

    override suspend fun getHeadlines(): Flow<NewsApiResponse>  {
        return flow {
            emit(api.getTopHeadLines())
        }.flowOn(Dispatchers.IO)
    }
}