package com.example.composenews.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composenews.models.Article
import com.example.composenews.models.FakeArticle

@ExperimentalCoilApi
@Composable
fun HeadlineItem(article: Article) {
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
@Preview
fun HeadlineItemPreview() {
    HeadlineItem(article = FakeArticle)
}

