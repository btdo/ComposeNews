package com.example.composenews.network

import com.example.composenews.models.NewsApiResponse
import com.example.composenews.models.SortBy
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") query: String? = null,
        @Query("sortBy") sortBy: String? = SortBy.publishedAt.name,
        @Query("pageSize") pageSize: Int? = 20,
        @Query("page") page: Int? = 1
    ): NewsApiResponse

    @GET("/v2/top-headlines")
    suspend fun getTopHeadLines(
        @Query("country") country: String? = "ca",
        @Query("sortBy") sortBy: String? = SortBy.publishedAt.name,
        @Query("category") category: String? = null,
        @Query("pageSize") pageSize: Int? = 20,
        @Query("page") page: Int? = 1
    ): NewsApiResponse

}