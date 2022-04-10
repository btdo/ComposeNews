package com.example.composenews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.AppResult
import com.example.composenews.models.ArticleUI
import com.example.composenews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleScreenViewModel @Inject constructor(
    private val repository: NewsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _article = MutableStateFlow<AppResult<ArticleUI>>(AppResult.Loading)
    val article: StateFlow<AppResult<ArticleUI>> = _article
    fun findArticle(articleId: String) {
        viewModelScope.launch {
            _article.value = AppResult.Success(repository.getArticle(articleId = articleId))
        }
    }
}