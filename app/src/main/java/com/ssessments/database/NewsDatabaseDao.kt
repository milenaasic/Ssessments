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

    @Insert
     suspend fun insertFilter(item: FilterItem)

    @Query("SELECT * FROM saved_filters_table ORDER BY id DESC")
     fun getAllFilters():LiveData<List<FilterItem>>

    @Query("DELETE from saved_filters_table")
    fun clearFiltersTable()

}