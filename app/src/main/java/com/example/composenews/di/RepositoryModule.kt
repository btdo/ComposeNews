package com.example.composenews.di

import com.example.composenews.db.ArticleDao
import com.example.composenews.network.NewsApi
import com.example.composenews.repository.ComposeNewsRepository
import com.example.composenews.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        articleDao: ArticleDao,
        newsApi: NewsApi,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): NewsRepository {
        return ComposeNewsRepository(articleDao, newsApi, dispatcher)
    }

}