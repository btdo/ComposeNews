package com.example.composenews.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composenews.models.NewsApiResponse

@Composable
fun HeadlinesScreen(news: NewsApiResponse) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Home Screen")
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HeadlinesScreen(NewsApiResponse(listOf(), "OK", 0))
}