package com.example.composenews.models

import com.example.composenews.db.ArticleEntity

data class HeadlinesUI(val topHeadline: ArticleUI, val otherHeadlines: List<ArticleUI>) {
    companion object {
        fun fromNetworkResponse(response: NewsApiResponse): HeadlinesUI {
            val topHeadline = ArticleUI.fromArticle(response.articles[0])
            val articleUIs = (1 until 4).map {
                response.articles[it]
            }.map {
                ArticleUI.fromArticle(it)
            }
            return HeadlinesUI(topHeadline = topHeadline, otherHeadlines = articleUIs)
        }
    }
}

data class HomeUI(val headlines: HeadlinesUI, val popular: OtherNews, val bookmarked: OtherNews)

data class OtherNews(val articles: List<ArticleUI>) {
    companion object {
        fun fromNetworkResponse(response: NewsApiResponse): OtherNews {
            val articles = response.articles.take(5).map {
                ArticleUI.fromArticle(it)
            }
            return OtherNews(articles)
        }

        fun fromDaoData(articles: List<ArticleEntity>): OtherNews {
            return OtherNews(articles.map {
                ArticleUI.fromArticleEntity(it)
            })
        }
    }
}

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