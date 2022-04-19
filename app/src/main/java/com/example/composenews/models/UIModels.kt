package com.example.composenews.models


import com.example.composenews.db.ArticleEntity
import com.example.composenews.db.formatTo
import com.example.composenews.db.md5Hash
import java.util.*
import kotlin.math.min

data class HeadlinesUI(val topHeadline: ArticleUI, val otherHeadlines: List<ArticleUI>) {
    companion object {
        fun fromArticles(articles: List<ArticleUI>): HeadlinesUI {
            val topHeadline = articles[0]
            val limit = min(articles.size, 4)
            val others = (1 until limit).map {
                articles[it]
            }
            return HeadlinesUI(topHeadline = topHeadline, otherHeadlines = others)
        }
    }
}

data class HomeState(
    val result: AppResult<Unit>,
    val headlines: NewsUI,
    val interested: NewsUI,
    val bookmarks: NewsUI
)

data class HomeUI(val headlines: HeadlinesUI, val interested: NewsUI, val bookmarks: NewsUI)

data class NewsUI(val articles: List<ArticleUI>)

data class ArticleUI(
    val id: String,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String,
    val source: String,
    val title: String,
    val url: String,
    val urlToImage: String? = null,
    val fromTopic: String? = null,
    val category: Category,
    val type: ArticleType,
    val isBookMarked: Boolean = false
) {
    companion object {
        fun fromArticleEntity(entity: ArticleEntity): ArticleUI {
            return ArticleUI(
                id = entity.id,
                author = entity.author,
                content = entity.content,
                description = entity.description,
                publishedAt = Date(entity.publishedAt).formatTo(),
                source = entity.source,
                title = entity.title,
                url = entity.url,
                urlToImage = entity.urlToImage,
                category = Category.valueOf(entity.category),
                type = ArticleType.valueOf(entity.type),
                isBookMarked = entity.isBookMarked
            )
        }

        fun fromNetworkArticle(
            networkArticle: NetworkArticle,
            category: Category,
            articleType: ArticleType
        ): ArticleUI {
            return ArticleUI(
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
                type = articleType,
                isBookMarked = false
            )
        }
    }
}

enum class ViewMoreCategory {
    Headlines, Topics, Bookmarks
}