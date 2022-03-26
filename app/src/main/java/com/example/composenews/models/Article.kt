package com.example.composenews.models

data class Article(
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String? = null,
    val isBookMarked: Boolean = false
)