package com.ssessments.newsapp.news_list_home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.newsapp.data.fakeNetworkNewFilterOBject
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.convertNetworkToDatabaseNewsItem
import com.ssessments.newsapp.utilities.isOnline
import kotlinx.coroutines.*


private const val mytag="MY_MAINFRAGM_ViewModel"
class MainFragmentViewModel(
                            val database:NewsDatabaseDao,
                            application:Application): AndroidViewModel(application) {


    //promenljiva koja sadrzi newsListu iz baze
    val newsList=database.getAllNews()

    private val _swiperefreshfinished = MutableLiveData<Boolean>()
    val swiperefreshfinished: LiveData<Boolean>
        get() = _swiperefreshfinished

    private val _showToastnoInternet = MutableLiveData<Boolean>()
    val showToastnoInternet: LiveData<Boolean>
        get() = _showToastnoInternet

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _newsToBeOpenedID = MutableLiveData<Int>()
    val newsToBeOpenedID: LiveData<Int>
        get() = _newsToBeOpenedID

    init {

        initializeNewsList(false)
        _newsToBeOpenedID.value = -1
    }

    fun initializeNewsList(initializedFromSwipeRefresh:Boolean) {
        if(!initializedFromSwipeRefresh)_showProgressBar.value=true

        if(isOnline(getApplication())){
            getFilteredNewsListFromServer(fakeNetworkNewFilterOBject,initializedFromSwipeRefresh)
        }else{
            Log.i(mytag,("no internet"))
            if(!initializedFromSwipeRefresh) _showProgressBar.value=false
            _showToastnoInternet.value=true
        }

    }

   fun getFilteredNewsListFromServer(filter: NetworkNewsFilterObject,initializedFromSwipeRefresh:Boolean){

        Log.i(mytag,("initializeNewsLis is online"))

        viewModelScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.postFilteredNewsList(fakeNetworkNewFilterOBject)
            try {
                var listResult = getPropertiesDeferred.await()
                Log.i(mytag,("result je :$listResult"))
                insertNewsIntoDatabase(listResult)
                if(!initializedFromSwipeRefresh) _showProgressBar.value=false
                else _swiperefreshfinished.value=true

            } catch (e: Exception) {
                val responseError="Failure"+e.message
                Log.i(mytag,("$responseError"))
                _networkError.value = true
                // proba dok ne proradi server
                insertNewsIntoDatabase(getNewsItemArray())
                // ubaci praznu listu
                //insertNewsIntoDatabase(mutableListOf())
                if(!initializedFromSwipeRefresh) _showProgressBar.value=false
                else _swiperefreshfinished.value=true
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

    fun setSwiperefreshedfinishedToFalse(){
        _swiperefreshfinished.value=false
    }


    fun noInternetSnackBarShown(){
        _showToastnoInternet.value=false
    }


    fun fetchNewsWithID(newsid:Int){
        _newsToBeOpenedID.value=newsid

    }

    fun resetNewsID() {
            _newsToBeOpenedID.value = -1
        }



}