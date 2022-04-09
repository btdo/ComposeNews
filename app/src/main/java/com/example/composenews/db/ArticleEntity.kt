package com.example.composenews.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composenews.models.NetworkArticle
import java.math.BigInteger
import java.security.MessageDigest

@Entity
data class ArticleEntity(
    @PrimaryKey
    val id: String,
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
    val category: String,
    val type: String,
    val date: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromNetworkArticle(
            networkArticle: NetworkArticle,
            category: String,
            type: String
        ): ArticleEntity {
            return ArticleEntity(
                id = md5Hash(networkArticle.title),
                author = networkArticle.author,
                content = networkArticle.content,
                description = networkArticle.description,
                publishedAt = networkArticle.publishedAt,
                source = networkArticle.source.name,
                title = networkArticle.title,
                url = networkArticle.url,
                urlToImage = networkArticle.urlToImage,
                category = category,
                type = type,
                isBookMarked = false
            )
        }


    }
}

fun md5Hash(str: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}