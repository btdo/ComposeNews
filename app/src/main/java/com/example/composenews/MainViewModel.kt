package com.example.composenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.NetworkResult
import com.example.composenews.models.NewsApiResponse
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
    val searchedNews: StateFlow<NetworkResult<NewsApiResponse>> = _query.debounce(1000).filter {
        return@filter it.isNotEmpty()
    } .flatMapLatest {
        repository.searchNews(it)
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NetworkResult.Loading)

    fun searchedNews(query: String) {
        viewModelScope.launch(defaultDispatcher) {
            repository.searchNews(query)
        }
    }
}