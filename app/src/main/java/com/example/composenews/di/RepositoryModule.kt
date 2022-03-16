package com.example.composenews.di

import com.example.composenews.network.NewsApi
import com.example.composenews.repository.ComposeChatRepository
import com.example.composenews.repository.ComposeChatRepositoryImpl
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
    fun provideRepository(newsApi : NewsApi, @DefaultDispatcher dispatcher: CoroutineDispatcher): ComposeChatRepository{
        return ComposeChatRepositoryImpl(newsApi, dispatcher)
    }

}