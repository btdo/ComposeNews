package com.example.composenews.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composenews.R
import com.example.composenews.models.FakeNewsApiResponse
import com.example.composenews.models.NewsApiResponse

@Composable
fun HeadlinesScreen(headlines: NewsApiResponse, modifier: Modifier = Modifier) {
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
        Spacer(modifier = Modifier.height(12.dp))
        HeadlineItem(article = headlines.articles[0])
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HeadlinesScreen(FakeNewsApiResponse)
}