package com.ssessments.newsapp.database

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

    @Query("SELECT COUNT (id) FROM last_news_list_table")
    suspend fun getNumberOfRowsInNewsListTable():Int




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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user:UserData)

    @Query("SELECT * FROM user_data_table" )
    suspend fun getUserNoLiveData():UserData

    @Delete
    suspend fun deleteUser(item:UserData)

    @Query("DELETE from user_data_table")
    suspend fun clearUserDataTable()

    @Query("SELECT * FROM user_data_table" )
    fun getUser():LiveData<UserData>

    @Query("SELECT COUNT (username) FROM user_data_table")
    suspend fun getNumberOfUsers():Int

    @Query("UPDATE user_data_table SET firebase_id=:firebaseId WHERE ID=1")
    suspend fun updateFirebaseIdInUserTable(firebaseId:String)

    @Query("UPDATE user_data_table SET username=:username,password=:password,token=:token WHERE ID=1")
    suspend fun updateUsernamePasswordTokenInUserTable(username:String,password:String,token:String)

    @Query("UPDATE user_data_table SET username=:username,password=:password,token=:token,first_name=:firstName,last_name=:lastName,access_type=:accessType, email=:email,mobile_phone=:phone,company=:company,country=:country WHERE ID=1")
    suspend fun updateUserAllButFirebaseId(username:String,password:String,token:String,firstName:String,lastName:String,accessType:String,email:String,phone:String,company:String,country:String)

    @Query("UPDATE user_data_table SET email=:email WHERE ID=1")
    suspend fun updateEmailInUserTable(email:String)

    @Query("UPDATE user_data_table SET mobile_phone=:phone WHERE ID=1")
    suspend fun updatePhoneInUserTable(phone:String)

    //CURRENT FILTER TABLE
    @Insert
    suspend fun insertCurrentFilterToDatabase(item: CurrentFilter)

    @Query("DELETE from current_filter_table")
    suspend fun clearCurrentFilterTable()

    @Query("SELECT * FROM current_filter_table WHERE ID=1")
    fun getCurrentFilterLiveData():LiveData<CurrentFilter>

    @Query("SELECT COUNT (id) FROM current_filter_table")
    suspend fun getNumberOfCurrentFilters():Int

    @Query("SELECT * FROM current_filter_table WHERE ID=1")
    suspend fun getCurrentFilterWithId():CurrentFilter

    @Query("SELECT * FROM current_filter_table WHERE language='English'")
    suspend fun getCurrentFilterWithLanguage():CurrentFilter

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentFilter(filter:CurrentFilter)


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