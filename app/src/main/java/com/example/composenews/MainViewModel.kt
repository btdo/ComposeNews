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

    fun addOrRemoveBookmark(articleUI: ArticleUI) {
        viewModelScope.launch(defaultDispatcher) {
            repository.bookmarkArticle(articleUI)
            repository.getBookmarkedArticles().collect { bookMarked ->
                _homeUIState.update { it ->
                    val currentResult = it as QueryResult.Success
                    QueryResult.Success(
                        markBookMarks(
                            currentResult.data.headlines,
                            currentResult.data.popular,
                            OtherNews.fromDaoData(bookMarked)
                        )
                    )
                }
            }
        }
    }

    private fun getHomeUiState() {
        viewModelScope.launch(defaultDispatcher) {
            val topic = repository.interestedTopics.first()
            combine(
                repository.getHeadlines(),
                repository.getHeadlines(category = topic, sortBy = SortBy.popularity),
                repository.getBookmarkedArticles()
            ) { headlines, popular, bookmarked ->
                val headlinesArticles = HeadlinesUI.fromNetworkResponse(headlines)
                val popularArticles = OtherNews.fromNetworkResponse(popular)
                val bookmarkedArticles = OtherNews.fromDaoData(bookmarked)
                QueryResult.Success(
                    markBookMarks(
                        headlinesArticles,
                        popularArticles,
                        bookmarkedArticles
                    )
                )
            }.catch { exception: Throwable ->
                _homeUIState.value = QueryResult.Error(exception = exception)
            }.onStart {
                QueryResult.Loading
            }.collect {
                _homeUIState.value = it
            }
        }
    }

    private fun markBookMarks(
        headlinesArticles: HeadlinesUI,
        popularArticles: OtherNews,
        bookmarkArticles: OtherNews
    ): HomeUI {
        val bookmarksMap = bookmarkArticles.articles.map { it.title to it }.toMap()
        val newTopHeadline =
            if (bookmarksMap.containsKey(headlinesArticles.topHeadline.title)) headlinesArticles.topHeadline.copy(
                isBookMarked = true
            ) else headlinesArticles.topHeadline
        val newHeadlines = headlinesArticles.otherHeadlines.map {
            if (bookmarksMap.containsKey(it.title)) it.copy(isBookMarked = true) else it
        }
        val newHeadlineArticles = HeadlinesUI(newTopHeadline, newHeadlines)

        val popular = popularArticles.articles.map {
            if (bookmarksMap.containsKey(it.title)) it.copy(isBookMarked = true) else it
        }
        val newPopular = popularArticles.copy(articles = popular)

        return HomeUI(newHeadlineArticles, newPopular, bookmarkArticles)
    }
}