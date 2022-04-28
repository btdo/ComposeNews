package com.example.composenews.models

val FakeArticle = NetworkArticle(
    title = "Russian forces pressed their assault on Ukrainian cities Friday, with new missile strikes and shelling on the capital Kyiv and the outskirts",
    description = "Russian forces pressed their assault on Ukrainian cities Friday, " +
            "with new missile strikes and shelling on the capital Kyiv and the outskirts " +
            "of the western city of Lviv, as world leaders pushed for an investigation " +
            "into the Kremlin's repeated attacks on civil…",
    url = "https://www.cbc.ca/news/world/russia-ukraine-missiles-airstrikes-war-1.6389343",
    urlToImage = "https://i.cbc.ca/1.6389383.1647602981!/fileImage/httpImage/image.JPG_gen/derivatives/16x9_620/ukraine-crisis.JPG",
    publishedAt = "2022-03-18T20:21:41Z",
    author = "Andrew Yeung",
    content = "The latest:\r\n<ul><li>Russian military fires missiles at airport near western city of Lviv, shelling reported in Kyiv area.</li><li>Ukrainian official says 130 people have been rescued from rubble of … [+9182 chars]",
    source = Source(id = "cbc-news", name = "CBC News")
)

val FakeArticle2 = NetworkArticle(
    title = "Court grants immediate injunction on Beltline protests in Calgary - CBC.ca",
    description = "The City of Calgary has been granted a temporary court injunction to address protesters in the city’s Beltline area.",
    url = "https://www.cbc.ca/news/canada/calgary/court-injunction-calgary-beltline-1.6390183",
    urlToImage = "https://i.cbc.ca/1.6383475.1647382919!/cumulusImage/httpImage/image.jpg_gen/derivatives/16x9_620/protesters-police-clash-in-calgary-s-beltline.jpg",
    publishedAt = "2022-03-18T20:14:24Z",
    content = "In an unorthodox diplomatic move, Canada's UN mission on Thursday tweeted out a heavily annotated letter Russia had sent to the United Nations, including in the rewrite pointed comments, later prompting Russian accusations of \"kindergarten-level libel.\"",
    source = Source(id = "cbc-news", name = "CBC News")
)

val responseApi = NewsApiResponse(listOf(FakeArticle, FakeArticle2), status = "ok", 2)

val articleUI1 = ArticleUI.fromNetworkArticle(FakeArticle, Category.general, ArticleType.headline)
val articleUI2 = ArticleUI.fromNetworkArticle(FakeArticle2, Category.general, ArticleType.headline)

val FakeHeadlinesUI = HeadlinesUI(
    ArticleUI.fromNetworkArticle(FakeArticle, Category.general, ArticleType.headline),
    listOf(
        ArticleUI.fromNetworkArticle(FakeArticle2, Category.general, ArticleType.headline),
        ArticleUI.fromNetworkArticle(FakeArticle2, Category.general, ArticleType.headline)
            .copy(title = "Another fake article", isBookMarked = true)
    )
)
val FakeHomeUIState = HomeState(
    AppResult.Success(Unit),
    NewsUI(
        listOf(
            ArticleUI.fromNetworkArticle(FakeArticle, Category.general, ArticleType.headline),
            ArticleUI.fromNetworkArticle(FakeArticle2, Category.general, ArticleType.headline),
            ArticleUI.fromNetworkArticle(FakeArticle2, Category.general, ArticleType.headline)
                .copy(title = "Another fake article", isBookMarked = true)
        )
    ),
    NewsUI(
        listOf(
            ArticleUI.fromNetworkArticle(
                FakeArticle,
                Category.business,
                ArticleType.topic
            ), ArticleUI.fromNetworkArticle(FakeArticle2, Category.business, ArticleType.topic)
        )
    ),
    NewsUI(
        listOf(
            ArticleUI.fromNetworkArticle(
                FakeArticle2,
                Category.business,
                ArticleType.topic
            )
        )
    )
)