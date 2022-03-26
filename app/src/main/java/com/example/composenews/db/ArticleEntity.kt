package com.example.composenews.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleEntity(
    @PrimaryKey
    val title: String,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String,
    val source: String,
    val url: String,
    val urlToImage: String? = null,
    val fromTopic: String? = null,
    val isBookMarked: Boolean = false,
    val dateBookMarked: Long = System.currentTimeMillis()
)