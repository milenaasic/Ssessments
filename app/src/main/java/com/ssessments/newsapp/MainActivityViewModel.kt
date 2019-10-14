package com.ssessments.newsapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.newsapp.data.fakeNetworkNewFilterOBject
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.NetworkCustomSearchFilterObject
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import com.ssessments.newsapp.utilities.convertNetworkToDatabaseNewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MY_TAG="MY_MainActivViewModel"

class MainActivityViewModel(val database:NewsDatabaseDao,
                            application: Application): AndroidViewModel(application){


    var loggedInUser=database.getUser()

    private val _showProgressBarMainActivity = MutableLiveData<Boolean>()
    val showProgressBarMainActivity: LiveData<Boolean>
        get() = _showProgressBarMainActivity

    private val _networkErrorMainActivity = MutableLiveData<Boolean>()
    val networkErrorMainActivity: LiveData<Boolean>
        get() = _networkErrorMainActivity

    fun clearUser(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.clearUserDataTable()
                Log.i(MY_TAG, "claer user table}")

            }
        }

    }

    fun setCurrentFilterAccordingToNotifications(currentFilter:CurrentFilter){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.clearCurrentFilterTable()
                database.insertCurrentFilterToDatabase(currentFilter)
                Log.i(MY_TAG, "claer filter table}")

            }
        }

    }

    fun doCustomNewsSearch(searchString:String){

        _showProgressBarMainActivity.value=true

        val mytoken=loggedInUser.value?.token ?: EMPTY_TOKEN

        viewModelScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.postCustomSearchNewsList(NetworkCustomSearchFilterObject(token=mytoken,searchBy = searchString))
            try {
                var listResult = getPropertiesDeferred.await()
                Log.i(MY_TAG, ("result je :$listResult"))
                insertNewsIntoDatabase(listResult)
                 _showProgressBarMainActivity.value = false

            } catch (e: Exception) {
                val responseError = "Failure" + e.message
                Log.i(MY_TAG, ("iz custom search: $responseError"))
                _networkErrorMainActivity.value = true
                // proba dok ne proradi server
                //insertNewsIntoDatabase(getNewsItemArray())
                // ubaci praznu listu
                insertNewsIntoDatabase(mutableListOf())
                _showProgressBarMainActivity.value = false
            }
        }
    }

    private fun insertNewsIntoDatabase(list:List<NetworkNewsItem>){
        if(list!=null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database.clearNewsTable()
                    database.insertNews(convertNetworkToDatabaseNewsItem(list))
                }
            }
        }

    }


}