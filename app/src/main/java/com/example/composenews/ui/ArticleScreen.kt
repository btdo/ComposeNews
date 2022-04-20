package com.example.composenews.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composenews.collectAsStateLifeCycle
import com.example.composenews.models.AppResult
import com.example.composenews.models.ArticleUI

@Composable
fun ArticleScreen(articleId: String, viewModel: ArticleScreenViewModel = hiltViewModel()) {
    val homeUI by viewModel.article.collectAsStateLifeCycle()
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.findArticle(articleId)
    })

    when (homeUI) {
        is AppResult.Loading -> {
            LoadingScreen()
        }
        is AppResult.Success -> {
            ArticleScreenContent(articleUI = (homeUI as AppResult.Success).data)
        }
        is AppResult.Error -> {
            ErrorScreen()
        }
    }
}

@Composable
fun ArticleScreenContent(articleUI: ArticleUI) {
    WebviewPage(url = articleUI.url)
}