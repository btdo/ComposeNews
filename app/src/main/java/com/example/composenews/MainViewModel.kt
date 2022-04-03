package com.example.composenews

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
class MainViewModel @Inject constructor(
    private val repository: NewsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _query = MutableSharedFlow<String>(replay = 0)
    val searchedNews: Flow<QueryResult<List<ArticleUI>>> = _query.debounce(1000).filter {
        return@filter it.isNotEmpty()
    }.flatMapLatest {
        flowOf(QueryResult.Success(repository.everything(it)))
    }.catch { exception: Throwable ->
        QueryResult.Error(exception = exception)
    }.onStart {
        QueryResult.Loading
    }

    fun searchNews(query: String) {
        viewModelScope.launch(defaultDispatcher) {
            _query.emit(query)
        }
    }
}