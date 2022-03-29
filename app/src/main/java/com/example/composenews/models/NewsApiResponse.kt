package com.example.composenews.models

data class NewsApiResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
) {
    fun toList(): List<ArticleUI> {
        if (status != "ok") {
            throw Exception("Error in response with ${status}")
        }
        val articles = articles.take(5).map {
            ArticleUI.fromArticle(it)
        }
        return articles
    }
}