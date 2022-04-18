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
import timber.log.Timber
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
        refreshNewsForHome()

        viewModelScope.launch(defaultDispatcher) {
            combine(
                repository.headlines,
                repository.interested,
                repository.bookmarks
            ) { headlines, interested, bookmarked ->
                if (headlines.isEmpty()) {
                    return@combine AppResult.Loading
                }

                val headlinesArticles = HeadlinesUI.fromArticles(headlines)
                val interestedArticles = OtherNews(interested.take(5))
                val bookmarkedArticles = OtherNews(bookmarked.take(5))
                AppResult.Success(
                    HomeUI(
                        headlinesArticles,
                        interestedArticles,
                        bookmarkedArticles
                    )
                )
            }.catch { exception ->
                _homeUIState.value = AppResult.Error(exception = exception)
                Timber.e(exception)
            }.flowOn(defaultDispatcher).collect {
                _homeUIState.value = it
            }
        }
    }

    private fun refreshNewsForHome() {
        viewModelScope.launch {
            try {
                repository.refreshNews()
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