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

    private val _homeUIState = MutableStateFlow<QueryResult<HomeUI>>(QueryResult.Loading)
    val homeUIState: StateFlow<QueryResult<HomeUI>>
        get() = _homeUIState

    init {
        viewModelScope.launch {
            combine(
                repository.headlines,
                repository.popular,
                repository.bookmarks
            ) { headlines, popular, bookmarked ->
                if (headlines.isEmpty()) {
                    return@combine QueryResult.Loading
                }

                val headlinesArticles = HeadlinesUI.fromArticles(headlines)
                val popularArticles = OtherNews(popular)
                val bookmarkedArticles = OtherNews(bookmarked)
                QueryResult.Success(
                    markBookMarks(
                        headlinesArticles,
                        popularArticles,
                        bookmarkedArticles
                    )
                )
            }.catch { exception ->
                _homeUIState.value = QueryResult.Error(exception = exception)
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
                _homeUIState.value = QueryResult.Error(exception = e)
            }
        }
    }

    fun addOrRemoveBookmark(articleUI: ArticleUI) {
        viewModelScope.launch(defaultDispatcher) {
            repository.bookmarkArticle(articleUI)
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