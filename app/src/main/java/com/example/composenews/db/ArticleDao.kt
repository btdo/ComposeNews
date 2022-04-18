package com.example.composenews.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Update
    fun update(article: ArticleEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(articles: List<ArticleEntity>)

    @Delete
    fun delete(article: ArticleEntity)

    @Query("Select * from ArticleEntity where type = :type order by publishedAt desc")
    fun getArticles(type: String): Flow<List<ArticleEntity>>

    @Query("Select * from ArticleEntity where isBookMarked = 1")
    fun getBookmarks(): Flow<List<ArticleEntity>>

    @Query("Select * from ArticleEntity where id = :id")
    fun getArticle(id: String): ArticleEntity

    @Query("Delete from ArticleEntity where isBookMarked != 1")
    fun deleteAllExceptBookmarks()
}