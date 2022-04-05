package com.example.composenews.models

data class NewsApiResponse(
    val articles: List<NetworkArticle>,
    val status: String,
    val totalResults: Int
) {
    fun toList(): List<ArticleUI> {
        if (status != "ok") {
            throw Exception("Error in response with $status")
        }
        val articles = articles.map {
            ArticleUI.fromArticle(it)
        }
        return articles
    }
}