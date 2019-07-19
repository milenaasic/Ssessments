package com.ssessments.news_list_home

import android.app.Application
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.ssessments.database.NewsItem
import com.ssessments.data.UserAccessRights
import com.ssessments.data.getNewsItemArray
import com.ssessments.database.NewsDatabase
import com.ssessments.network.NetworkNewsItem
import com.ssessments.network.NewsApi
import kotlinx.coroutines.*

class NewsListHome_ViewModel(application:Application): AndroidViewModel(application) {

    //val repository = NewsListHomeRepository(NewsDatabase.getInstance(application))
    //private var viewModelJob= Job()
    //private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)


    private val _newsList = MutableLiveData<List<NetworkNewsItem>>()
    val newsList: LiveData<List<NetworkNewsItem>>
        get() = _newsList

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val _showNoAccessRightsSnackbar = MutableLiveData<Boolean>()
    val showNoAccessRightsSnackbar: LiveData<Boolean>
        get() = _showNoAccessRightsSnackbar

    private val _newsToBeOpenedID = MutableLiveData<Long>()
    val newsToBeOpenedID: LiveData<Long>
        get() = _newsToBeOpenedID


    init {
        //ovde je sve radilo kada sam za newslist stavljala fake listu List<NewsItem> pre nego sto sam dodala bazu i inetrnet

        initializeNewsList()

        _showNoAccessRightsSnackbar.value = false
        _newsToBeOpenedID.value = -1L
    }

    private fun initializeNewsList() {
        // za kasnije ako prilikom inicijalizacije nema mreze podigni iz baze listu
        if (isOnline())

        viewModelScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.getNewsList()
            try {
                var listResult = getPropertiesDeferred.await()
                _newsList.value = listResult
            } catch (e: Exception) {
                _networkError.value = true
            }
        }

    }

    private fun isOnline(): Boolean {
        return true
    }



    /*private fun insertOneItem() {
        val item = NewsItem(
            1,
            1,
            "Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly",
            "31 May 2019, 18 : 38",
            "Premium Users"
        )
        viewModelScope.launch {
            insert(item)
        }*/


    /*private suspend fun insert(item: NewsItem) {
        withContext(Dispatchers.IO) {
            repository.insert(item)
        }
    }*/

        //otvara detail news fragment
        fun fetchNewsWithID(newsID: Long) {

            if (isNewsAccessibleByThisUser(UserAccessRights.PREMIUM_USER)) {
                //otvori detail news fragment
                 _newsToBeOpenedID.value= newsID

            } else {_showNoAccessRightsSnackbar.value = true}
        }

        fun noAccessRightsSnackbarShown() {
            // _showNoAccessRightsSnackbar.value=false
        }

        fun isNewsAccessibleByThisUser(userType: UserAccessRights): Boolean {
            return true
        }

        fun resetNewsID() {
            _newsToBeOpenedID.value = -1L
        }

        override fun onCleared() {
            super.onCleared()
            //viewModelJob.cancel()
        }

}