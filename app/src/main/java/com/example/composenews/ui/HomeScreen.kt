package com.example.composenews.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.FakeHomeUIState
import com.example.composenews.models.HomeUI

@Composable
fun HomeScreen(homeUI: HomeUI, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
            .padding(12.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Text(
            text = stringResource(id = R.string.home_top_section_title),
            style = MaterialTheme.typography.subtitle1
        )
        HeadlineItem(article = homeUI.headlines.topHeadline)
        SectionDivider()
        homeUI.headlines.otherHeadlines.forEach {
            NewsListItem(article = it, onSelect = {})
            SectionDivider()
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(FakeHomeUIState)
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
        Text(text = article.title, style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        article.author?.let {
            Text(text = article.author, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = article.publishedAt, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun NewsListItem(article: ArticleUI, onSelect: (ArticleUI) -> Unit) {
    val painter = rememberImagePainter(data = article.urlToImage)
    val imageModifier = Modifier
        .size(40.dp, 40.dp)
        .clip(shape = MaterialTheme.shapes.small)
    Column {
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
            BookmarkButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                selected = article.isBookMarked
            ) {
                onSelect(article)
            }
        }
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
