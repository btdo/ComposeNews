package com.example.composenews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.AppResult
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.HomeState
import com.example.composenews.models.NewsUI
import com.example.composenews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: NewsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeState(
            AppResult.Loading,
            NewsUI(listOf()),
            NewsUI(listOf()),
            NewsUI(listOf())
        )
    )
    val uiState: StateFlow<HomeState> = _uiState

    init {
        refreshNewsForHome()
        viewModelScope.launch(defaultDispatcher) {
            repository.interested.collect {
                _uiState.value = _uiState.value.copy(interested = NewsUI(it.take(5)))
            }
        }
        viewModelScope.launch(defaultDispatcher) {
            repository.bookmarks.collect {
                _uiState.value = _uiState.value.copy(bookmarks = NewsUI(it.take(5)))
            }
        }
        viewModelScope.launch {
            repository.headlines.collect {
                if (it.isEmpty()) {
                    _uiState.value = _uiState.value.copy(result = AppResult.Loading)
                    return@collect
                }
                _uiState.value = _uiState.value.copy(
                    result = AppResult.Success(Unit),
                    headlines = NewsUI(it.take(5))
                )
            }
        }
    }

    private fun refreshNewsForHome() {
        viewModelScope.launch {
            try {
                repository.refreshNews()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(result = AppResult.Error(e))
            }
        }
    }

    fun addOrRemoveBookmark(articleUI: ArticleUI) {
        viewModelScope.launch(defaultDispatcher) {
            repository.bookmarkArticle(articleUI)
        }
    }
}