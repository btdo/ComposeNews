package com.example.composenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.*
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
    private val _query = MutableSharedFlow<String>(replay = 0)
    val searchedNews: Flow<QueryResult<NewsApiResponse>> = _query.debounce(1000).filter {
        return@filter it.isNotEmpty()
    }.flatMapLatest {
        repository.everything(it)
    }.flatMapLatest {
        flowOf(QueryResult.Success(it))
    }.catch { exception: Throwable ->
        QueryResult.Error(exception = exception)
    }.onStart {
        QueryResult.Loading
    }

    private val _homeUIState =
        MutableStateFlow<QueryResult<HomeUI>>(QueryResult.Loading)
    val homeUIState: StateFlow<QueryResult<HomeUI>>
        get() = _homeUIState

    init {
        getHomeUiState()
    }

    fun searchNews(query: String) {
        viewModelScope.launch(defaultDispatcher) {
            _query.emit(query)
        }
    }

    private fun getHomeUiState() {
        viewModelScope.launch(defaultDispatcher) {
            val topic = repository.interestedTopics.first()
            combine(
                repository.getHeadlines(),
                repository.everything(sortBy = SortBy.popularity),
                repository.everything(query = topic.name)
            ) { headlines, popular, interested ->
                QueryResult.Success(
                    HomeUI(
                        HeadlinesUI.fromNetworkResponse(headlines),
                        OtherNews.fromNetworkResponse(popular),
                        OtherNews.fromNetworkResponse(interested)
                    )
                )
            }.catch { exception: Throwable ->
                QueryResult.Error(exception = exception)
            }.onStart {
                QueryResult.Loading
            }.collect {
                _homeUIState.value = it
            }
        }
    }
}