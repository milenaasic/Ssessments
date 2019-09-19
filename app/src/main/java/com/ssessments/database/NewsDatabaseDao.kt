package com.ssessments.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.Deferred


@Dao
interface NewsDatabaseDao{

    //NEWS TABLE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news:List<NewsItem>)

    @Query("SELECT * FROM last_news_list_table ORDER BY id" )
     fun getAllNews():LiveData<List<NewsItem>>

    @Query("DELETE from last_news_list_table")
     suspend fun clearNewsTable()



    //SAVED FILTERS TABLE
    @Insert
     suspend fun insertFilter(item: FilterItem)

    @Query("SELECT * FROM saved_filters_table ORDER BY id DESC")
     fun getAllFilters():LiveData<List<FilterItem>>

    @Query("SELECT * FROM saved_filters_table WHERE ID=:filterid")
    fun getChosenFilterFromSavesFilters(filterid:Long):FilterItem

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


    //CURRENT FILTER TABLE
    @Insert
    fun insertCurrentFilterToDatabase(item: CurrentFilter)

    @Query("DELETE from current_filter_table")
    fun clearCurrentFilterTable()

    @Query("SELECT * FROM current_filter_table")
    fun getCurrentFilter():LiveData<CurrentFilter>


    //PREDEFINED FILTERS TABLE
    @Query("SELECT * FROM predefined_filters_table ORDER BY id DESC")
    fun getAllPredefinedFilters():LiveData<List<PredefinedFilter>>

    @Query("DELETE FROM predefined_filters_table")
    fun clearPredefinedFiltersTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPredefinedFilters(list:List<PredefinedFilter>)

    @Query("SELECT * FROM predefined_filters_table WHERE ID=:filterid")
    fun getChosenFilterFromPredefinedFilters(filterid:Long):PredefinedFilter


}