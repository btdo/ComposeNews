package com.example.composenews.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Update
    suspend fun updateArticle(article: ArticleEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg articles: ArticleEntity)

    @Delete
    suspend fun delete(article: ArticleEntity)

    @Query("Select * from ArticleEntity ORDER BY dateBookMarked desc")
    fun getArticles(): Flow<List<ArticleEntity>>
}