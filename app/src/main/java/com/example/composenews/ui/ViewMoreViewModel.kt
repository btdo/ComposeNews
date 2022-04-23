package com.example.composenews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.ArticleUI
import com.example.composenews.models.ViewMoreCategory
import com.example.composenews.repository.DefaultPageSize
import com.example.composenews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewMoreViewModel @Inject constructor(
    private val repository: NewsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _articleList = MutableStateFlow<List<ArticleUI>>(listOf())
    val articleList: StateFlow<List<ArticleUI>> = _articleList

    init {
        viewModelScope.launch(defaultDispatcher) {
            val flow = when (repository.selectedViewMore.value) {
                ViewMoreCategory.Headlines -> repository.headlines
                ViewMoreCategory.Topics -> repository.interested
                ViewMoreCategory.Bookmarks -> repository.bookmarks
            }

            flow.collect {
                _articleList.value = it
            }
        }
    }

    fun loadMore() {
        if (getNextPage() == -1) {
            return
        }

        viewModelScope.launch(defaultDispatcher) {
            val nextPage = getNextPage()
            Timber.d("next page $nextPage")
            repository.loadHeadlines(page = nextPage)
        }
    }

    private fun getNextPage(): Int {
        if (articleList.value.size % DefaultPageSize == 0) {
            return (articleList.value.size / DefaultPageSize) + 1
        }

        return -1
    }
}