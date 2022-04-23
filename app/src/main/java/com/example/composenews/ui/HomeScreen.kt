package com.example.composenews.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composenews.R
import com.example.composenews.collectAsStateLifeCycle
import com.example.composenews.models.*

@ExperimentalCoilApi
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onArticleClicked: (ArticleUI) -> Unit,
    onViewMore: (ViewMoreCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val homeState by viewModel.uiState.collectAsStateLifeCycle()
    when (homeState.result) {
        is AppResult.Loading -> {
            LoadingScreen()
        }
        is AppResult.Success -> {
            HomeScreenMainContent(
                homeUI = homeState,
                onArticleClicked = onArticleClicked,
                onBookmarkSelected = viewModel::addOrRemoveBookmark,
                onViewMore = {
                    onViewMore(it)
                    viewModel.onViewMore(it)
                },
                modifier = modifier
            )
        }
        is AppResult.Error -> {
            ErrorScreen()
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HomeScreenMainContent(
    homeUI: HomeState, onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit,
    onViewMore: (ViewMoreCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
            .padding(12.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Headlines(
            headlines = HeadlinesUI.fromArticles(homeUI.headlines.articles),
            onArticleClicked = onArticleClicked,
            onBookmarkSelected = onBookmarkSelected, onViewMore = onViewMore
        )
        InterestedTopics(
            interestedTopics = homeUI.interested,
            onArticleClicked,
            onBookmarkSelected,
            onViewMore = onViewMore
        )
        Bookmarks(
            bookmarkedArticles = homeUI.bookmarks,
            onArticleClicked = onArticleClicked,
            onBookmarkSelected = onBookmarkSelected
        )
    }
}

@ExperimentalCoilApi
@Composable
fun Headlines(
    headlines: HeadlinesUI,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit,
    onViewMore: (ViewMoreCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionTitle(title = stringResource(id = R.string.home_top_section_title))
        Spacer(modifier = Modifier.height(12.dp))
        HeadlineItem(
            article = headlines.topHeadline,
            onArticleClicked = onArticleClicked,
            onBookmarkSelected = onBookmarkSelected
        )
        SectionDivider()
        headlines.otherHeadlines.forEach {
            ArticleListColumnItem(
                article = it,
                onArticleClicked = onArticleClicked,
                onBookmarkSelected = onBookmarkSelected
            )
            SectionDivider()
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ViewMoreButton {
                onViewMore(ViewMoreCategory.Headlines)
            }
        }
        SectionDivider()
    }
}

@Composable
fun ViewMoreButton(onViewMore: () -> Unit) {
    TextButton(onClick = onViewMore) {
        Text(
            text = stringResource(id = R.string.view_more),
            style = MaterialTheme.typography.button
        )
    }
}

@ExperimentalCoilApi
@Composable
fun Bookmarks(
    bookmarkedArticles: NewsUI,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit, modifier: Modifier = Modifier
) {
    if (bookmarkedArticles.articles.isEmpty()) {
        return
    }

    Column {
        SectionTitle(title = stringResource(id = R.string.home_bookmarked_section_title))
        Spacer(modifier = Modifier.height(12.dp))
        bookmarkedArticles.articles.forEach {
            ArticleListColumnItem(
                article = it,
                onArticleClicked = onArticleClicked,
                onBookmarkSelected = onBookmarkSelected
            )
            SectionDivider()
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun InterestedTopics(
    interestedTopics: NewsUI,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit,
    onViewMore: (ViewMoreCategory) -> Unit
) {
    SectionTitle(title = stringResource(id = R.string.home_interested_topics_title))
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        interestedTopics.articles.forEach { article ->
            Card(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(280.dp, 240.dp)
            ) {
                ArticleListRowItem(
                    article = article,
                    onArticleClicked = onArticleClicked,
                    onBookmarkSelected = onBookmarkSelected
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        Card(shape = MaterialTheme.shapes.small, modifier = Modifier.size(60.dp, 240.dp)) {
            ViewMoreButton(onViewMore = { onViewMore(ViewMoreCategory.Topics) })
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    SectionDivider()
}

@ExperimentalCoilApi
@Composable
@Preview
private fun PopularStoriesPreview() {
    InterestedTopics(
        interestedTopics = FakeHomeUIState.interested,
        onArticleClicked = {},
        onBookmarkSelected = {},
        onViewMore = {})
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6
    )
}


@ExperimentalCoilApi
@Composable
fun HeadlineItem(
    article: ArticleUI, onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit
) {
    val painter = rememberImagePainter(data = article.urlToImage)
    val imageModifier = Modifier
        .heightIn(min = 180.dp)
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.medium)
    Column(modifier = Modifier.clickable { onArticleClicked(article) }) {
        Image(
            painter = painter,
            contentDescription = article.description,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            ArticleItemContent(article = article, modifier = Modifier.weight(1f))
            BookmarkButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                selected = article.isBookMarked,
                onSelect = {
                    onBookmarkSelected(article)
                })
        }
    }
}


@ExperimentalCoilApi
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenMainContent(
        FakeHomeUIState,
        onArticleClicked = {},
        onBookmarkSelected = {},
        onViewMore = {})
}