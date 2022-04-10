package com.example.composenews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.*
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

    private val _homeUIState = MutableStateFlow<AppResult<HomeUI>>(AppResult.Loading)
    val homeUIState: StateFlow<AppResult<HomeUI>>
        get() = _homeUIState

    init {
        viewModelScope.launch {
            getNewsForHome()
        }
        viewModelScope.launch(defaultDispatcher) {
            combine(
                repository.articles,
                repository.bookmarks
            ) { articles, bookmarked ->
                if (articles.isEmpty()) {
                    return@combine AppResult.Loading
                }

                val headlines = articles.filter {
                    it.type == ArticleType.headline
                }
                val others = articles.filter {
                    it.type == ArticleType.topic
                }

                val headlinesArticles = HeadlinesUI.fromArticles(headlines)
                val popularArticles = OtherNews(others)
                val bookmarkedArticles = OtherNews(bookmarked)
                AppResult.Success(
                    HomeUI(
                        headlinesArticles,
                        popularArticles,
                        bookmarkedArticles
                    )
                )
            }.catch { exception ->
                _homeUIState.value = AppResult.Error(exception = exception)
            }.collect {
                _homeUIState.value = it
            }
        }
    }

    fun getNewsForHome() {
        viewModelScope.launch {
            try {
                repository.getNewsForHome()
            } catch (e: Exception) {
                _homeUIState.value = AppResult.Error(exception = e)
            }
        }
    }

    fun addOrRemoveBookmark(articleUI: ArticleUI) {
        viewModelScope.launch(defaultDispatcher) {
            repository.bookmarkArticle(articleUI)
        }
    }
}