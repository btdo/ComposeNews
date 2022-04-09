package com.example.composenews.models

import com.example.composenews.db.ArticleEntity

data class NewsApiResponse(
    val articles: List<NetworkArticle>,
    val status: String,
    val totalResults: Int
) {
    fun toList(category: Category, type: ArticleType): List<ArticleEntity> {
        if (status != "ok") {
            throw Exception("Error in response with $status")
        }
        val articles = articles.map {
            ArticleEntity.fromNetworkArticle(it, category = category.name, type = type.name)
        }
        return articles
    }
}