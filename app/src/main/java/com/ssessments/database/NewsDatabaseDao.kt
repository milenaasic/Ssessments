package com.ssessments.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy



@Dao
interface NewsDatabaseDao{

    //NEWS TABLE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news:List<NewsItem>)

    @Query("SELECT * FROM last_news_list_table ORDER BY id" )
     fun getAllNews():LiveData<List<NewsItem>>

    @Query("DELETE from last_news_list_table")
     suspend fun clearNewsTable()



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
    suspend fun insertUser(item: UserData)

    @Delete
    suspend fun deleteUser(item:UserData)

    @Query("DELETE from user_data_table")
    suspend fun clearUserDataTable()

    @Query("SELECT * FROM user_data_table" )
    fun getUser():LiveData<UserData>

    @Update
    suspend fun updateUser(user:UserData)

    @Query("SELECT COUNT (username) FROM user_data_table")
    suspend fun getNumberOfUsers():Int



}