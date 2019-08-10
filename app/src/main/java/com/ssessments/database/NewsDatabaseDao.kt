package com.ssessments.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDatabaseDao{

    //NEWS TABLE
    @Insert
    fun insert(item: NewsItem)

    @Query("SELECT * FROM last_news_list_table ORDER BY id" )
     fun getAllNews():List<NewsItem>

    @Query("DELETE from last_news_list_table")
     fun clear()

    //FILTER TABLE
    @Insert
     suspend fun insertFilter(item: FilterItem)

    @Query("SELECT * FROM saved_filters_table ORDER BY id DESC")
     fun getAllFilters():LiveData<List<FilterItem>>

     @Delete
     suspend fun deleteFilterItem(item:FilterItem)

    @Query("DELETE from saved_filters_table")
    fun clearFiltersTable()


    //USER TABLE
    @Insert
    fun insertUser(item: UserData)

    @Delete
    suspend fun deleteUser(item:UserData)

    @Query("DELETE from user_data_table")
    fun clearUserDataTable()

    @Query("SELECT * FROM user_data_table WHERE id=0" )
    fun getUser():UserData


}