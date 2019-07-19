package com.ssessments.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDatabaseDao{

    @Insert
    fun insert(item: NewsItem)

    @Query("SELECT * FROM last_news_list_table ORDER BY id" )
    fun getAllNews():List<NewsItem>

    @Query("DELETE from last_news_list_table")
    fun clear()



}