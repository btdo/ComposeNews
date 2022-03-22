package com.example.composenews.models

data class HeadlinesUI(val topHeadline: ArticleUI, val otherHeadlines: List<ArticleUI>) {
    companion object {
        fun fromNetworkResponse(response: NewsApiResponse): HeadlinesUI {
            val topHeadline = response.articles[0].toArticleUI()
            val articleUIs = (1 until 4).map {
                response.articles[it]
            }.map {
                it.toArticleUI()
            }
            return HeadlinesUI(topHeadline = topHeadline, otherHeadlines = articleUIs)
        }
    }
}

class HomeUI(val headlines: HeadlinesUI, val popular: OtherNews, val topicFavourites: OtherNews)

data class OtherNews(val articles: List<ArticleUI>) {
    companion object {
        fun fromNetworkResponse(response: NewsApiResponse): OtherNews {
            val articles = response.articles.take(5).map {
                it.toArticleUI()
            }
            return OtherNews(articles)
        }
    }
}

data class ArticleUI(
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String? = null,
    val fromTopic: String? = null,
    val isBookMarked: Boolean = false
)