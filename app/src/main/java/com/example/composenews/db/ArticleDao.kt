package com.example.composenews.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Update
    fun update(article: ArticleEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg articles: ArticleEntity)

    @Delete
    fun delete(article: ArticleEntity)

    @Query("Select * from ArticleEntity ORDER BY dateBookMarked desc")
    fun getArticles(): Flow<List<ArticleEntity>>
}