package com.example.composenews.network

import com.example.composenews.models.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): NewsApiResponse

    @GET("/v2/top-headlines")
    suspend fun getTopHeadLines(
        @Query("country") country: String? = "ca",
        @Query("category") category: String? = "ca",
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): NewsApiResponse

}