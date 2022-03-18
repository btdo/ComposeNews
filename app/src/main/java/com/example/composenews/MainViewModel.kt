package com.example.composenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.NewsApiResponse
import com.example.composenews.models.QueryResult
import com.example.composenews.repository.ComposeChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ComposeChatRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _query = MutableStateFlow("ukraine")
    val searchedNews: StateFlow<QueryResult<NewsApiResponse>> = _query.debounce(1000).filter {
        return@filter it.isNotEmpty()
    }.flatMapLatest {
        repository.searchNews(it)
    }.catch { exception: Throwable ->
        QueryResult.Error(exception = exception)
    }.onStart {
        QueryResult.Loading
    }.stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(5000L),
        initialValue = QueryResult.Loading
    )

    private val _topHeadLineNews =
        MutableStateFlow<QueryResult<NewsApiResponse>>(QueryResult.Loading)
    val topHeadlinesNews: StateFlow<QueryResult<NewsApiResponse>>
        get() = _topHeadLineNews

    init {
        getHeadlines()
    }

    fun searchNews(query: String) {
        _query.value = query
    }

    fun refreshHeadlines() {
        getHeadlines()
    }

    private fun getHeadlines() {
        viewModelScope.launch {
            repository.getHeadlines().catch { exception: Throwable ->
                _topHeadLineNews.value = QueryResult.Error(exception = exception)
            }.onStart {
                QueryResult.Loading
            }.collect {
                _topHeadLineNews.value = it
            }
        }
    }
}