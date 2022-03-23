package com.example.composenews.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composenews.R
import com.example.composenews.models.*

@ExperimentalCoilApi
@Composable
fun HomeScreen(
    homeUI: HomeUI,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit,
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
        TopStories(
            headlines = homeUI.headlines,
            onArticleClicked = onArticleClicked,
            onBookmarkSelected = onBookmarkSelected
        )
        PopularStories(popular = homeUI.popular, onArticleClicked, onBookmarkSelected)
    }
}

@ExperimentalCoilApi
@Composable
fun TopStories(
    headlines: HeadlinesUI, onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit,
) {
    SectionTitle(title = stringResource(id = R.string.home_top_section_title))
    Spacer(modifier = Modifier.height(12.dp))
    HeadlineItem(article = headlines.topHeadline)
    SectionDivider()
    headlines.otherHeadlines.forEach {
        ArticleListColumnItem(
            article = it,
            onArticleClicked = onArticleClicked,
            onBookmarkSelected = onBookmarkSelected
        )
        SectionDivider()
    }
}


@ExperimentalCoilApi
@Composable
private fun PopularStories(
    popular: OtherNews,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit
) {
    SectionTitle(title = stringResource(id = R.string.home_popular_section_title))
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        popular.articles.forEach { article ->
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
    }
    Spacer(modifier = Modifier.height(12.dp))
    SectionDivider()
}

@ExperimentalCoilApi
@Composable
@Preview
private fun PopularStoriesPreview() {
    PopularStories(
        popular = FakeHomeUIState.popular,
        onArticleClicked = {},
        onBookmarkSelected = {})
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6
    )
}

@ExperimentalCoilApi
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(FakeHomeUIState, {}, {})
}

@ExperimentalCoilApi
@Composable
fun HeadlineItem(article: ArticleUI) {
    val painter = rememberImagePainter(data = article.urlToImage)
    val imageModifier = Modifier
        .heightIn(min = 180.dp)
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.medium)
    Column {
        Image(
            painter = painter,
            contentDescription = article.description,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        ArticleItemContent(article = article)
    }
}

@ExperimentalCoilApi
@Composable
fun ArticleListRowItem(
    article: ArticleUI,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit
) {
    val painter = rememberImagePainter(data = article.urlToImage)
    val imageModifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .clip(shape = MaterialTheme.shapes.small)
    Column(modifier = Modifier.clickable {
        onArticleClicked(article)
    }) {
        Image(
            painter = painter,
            contentDescription = article.description,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            ArticleItemContent(article = article, modifier = Modifier.weight(1f))
            BookmarkButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                selected = article.isBookMarked
            ) {
                onBookmarkSelected(article)
            }
        }
    }
}

@Composable
fun ArticleItemContent(article: ArticleUI, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = article.source.name, style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.height(8.dp))
        ArticleItemDetails(article)
    }
}

@ExperimentalCoilApi
@Composable
fun ArticleListColumnItem(
    article: ArticleUI,
    onArticleClicked: (ArticleUI) -> Unit,
    onBookmarkSelected: (ArticleUI) -> Unit
) {
    val painter = rememberImagePainter(data = article.urlToImage)
    val imageModifier = Modifier
        .size(40.dp, 40.dp)
        .clip(shape = MaterialTheme.shapes.small)
    Column(modifier = Modifier.clickable { onArticleClicked(article) }) {
        Text(text = article.source.name, style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Image(
                painter = painter,
                contentDescription = article.description,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                ArticleItemDetails(article)
            }
            BookmarkButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                selected = article.isBookMarked
            ) {
                onBookmarkSelected(article)
            }
        }
    }
}

@Composable
private fun ArticleItemDetails(article: ArticleUI) {
    Text(text = article.title, style = MaterialTheme.typography.subtitle2)
    Spacer(modifier = Modifier.height(8.dp))
    article.author?.let {
        Text(text = article.author, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
    }
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(text = article.publishedAt, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    contentAlpha: Float = ContentAlpha.medium,
    onSelect: () -> Unit
) {
    CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
        IconButton(modifier = modifier, onClick = onSelect) {
            Icon(
                imageVector = if (selected) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null
            )
        }
    }
}

@Composable
fun SectionDivider() {
    Divider(
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}
