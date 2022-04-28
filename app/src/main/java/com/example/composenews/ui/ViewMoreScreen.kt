package com.example.composenews.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.composenews.collectAsStateLifeCycle
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.ViewMoreCategory
import com.example.composenews.models.articleUI1
import com.example.composenews.models.articleUI2

@ExperimentalCoilApi
@Composable
fun ViewMoreScreen(viewModel: ViewMoreViewModel = hiltViewModel(), category: ViewMoreCategory) {
    val articleList by viewModel.viewMoreList.collectAsStateLifeCycle(initial = listOf())
    ViewMoreContent(articleList, onArticleClicked = {}, onBookmarkSelected = {}, {
        viewModel.loadMore()
    })
}

@Composable
fun ViewMoreContent(
    articles: List<ArticleUI>, onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit, onLoadMore: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val loadMore by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    if (loadMore) {
        onLoadMore()
    }

    LazyColumn(
        state = scrollState,
        content = {
            items(items = articles, key = {
                it.id
            }) { article ->
                ArticleListColumnItem(
                    article = article,
                    onArticleClicked = onArticleClicked,
                    onBookmarkSelected = onBookmarkSelected
                )
                SectionDivider()
            }
        }, modifier = Modifier
            .fillMaxSize()
    )
}

fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@Preview
@Composable
fun ViewMoreContentPreview() {
    ViewMoreContent(
        articles = listOf(articleUI1, articleUI2),
        onArticleClicked = {},
        onBookmarkSelected = {},
        onLoadMore = {}
    )
}

