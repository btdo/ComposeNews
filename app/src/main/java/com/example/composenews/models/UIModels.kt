package com.example.composenews.models

import com.example.composenews.db.ArticleEntity

data class HeadlinesUI(val topHeadline: ArticleUI, val otherHeadlines: List<ArticleUI>) {
    companion object {
        fun fromArticles(articles: List<ArticleUI>): HeadlinesUI {
            val topHeadline = articles[0]
            val others = (1 until 4).map {
                articles[it]
            }
            return HeadlinesUI(topHeadline = topHeadline, otherHeadlines = others)
        }
    }
}

data class HomeUI(val headlines: HeadlinesUI, val popular: OtherNews, val bookmarked: OtherNews)

data class OtherNews(val articles: List<ArticleUI>)

data class ArticleUI(
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String,
    val source: String,
    val title: String,
    val url: String,
    val urlToImage: String? = null,
    val fromTopic: String? = null,
    val isBookMarked: Boolean = false
) {

    fun toArticleEntity(): ArticleEntity {
        return ArticleEntity(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source,
            title = title,
            urlToImage = urlToImage,
            url = url,
            isBookMarked = isBookMarked
        )
    }

    companion object {
        fun fromArticleEntity(entity: ArticleEntity): ArticleUI {
            return ArticleUI(
                author = entity.author,
                content = entity.content,
                description = entity.description,
                publishedAt = entity.publishedAt,
                source = entity.source,
                title = entity.title,
                url = entity.url,
                urlToImage = entity.urlToImage,
                isBookMarked = true
            )
        }

        fun fromArticle(article: Article): ArticleUI {
            return ArticleUI(
                author = article.author,
                content = article.content,
                description = article.description,
                publishedAt = article.publishedAt,
                source = article.source.name,
                title = article.title,
                url = article.url,
                urlToImage = article.urlToImage,
                isBookMarked = article.isBookMarked
            )
        }
    }
}

enum class ViewMoreCategory {
    Headlines, Topics, Bookmarks
}