package com.example.composenews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.di.DefaultDispatcher
import com.example.composenews.models.ArticleUI
import com.example.composenews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewMoreViewModel @Inject constructor(
    private val repository: NewsRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    val viewMoreList: Flow<List<ArticleUI>> = repository.viewMoreList.mapLatest {
        it.map { entity ->
            ArticleUI.fromArticleEntity(entity = entity)
        }
    }

    fun loadMore() {
        viewModelScope.launch(defaultDispatcher) {
            repository.loadMore()
        }
    }
}