package com.ssessments.newsapp.news_list_home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.convertNetworkToDatabaseNewsItem
import com.ssessments.newsapp.utilities.isOnline
import kotlinx.coroutines.*


private const val mytag="MY_MAINFRAGM_ViewModel"
class MainFragmentViewModel(
                            val database:NewsDatabaseDao,
                            application:Application): AndroidViewModel(application) {

    //val repository = NewsListHomeRepository(NewsDatabase.getInstance(application))
    //private var viewModelJob= Job()
    //private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    /*private val _newsList = MutableLiveData<List<NetworkNewsItem>>()
    val newsList: LiveData<List<NetworkNewsItem>>
        get() = _newsList*/


    //promenljiva koja sadrzi newsListu iz baze
    val newsList=database.getAllNews()

    private val _swiperefreshfinished = MutableLiveData<Boolean>()
    val swiperefreshfinished: LiveData<Boolean>
        get() = _swiperefreshfinished


    private val _noInternet = MutableLiveData<Boolean>()
    val noInternet: LiveData<Boolean>
        get() = _noInternet


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
        Log.i(mytag,("init"))
        _showProgressBar.value=false
        _noInternet.value=false
        initializeNewsList()
        _newsToBeOpenedID.value = -1
    }

    fun initializeNewsList() {
        Log.i(mytag,("initializeNewsLis"))
        if (isOnline(getApplication())){
            Log.i(mytag,("initializeNewsLis is online"))

            viewModelScope.launch {
                var getPropertiesDeferred = NewsApi.retrofitService.getNewsList()
                try {
                    Log.i(mytag,("preproeprtydeffered"))
                    var listResult = getPropertiesDeferred.await()
                    Log.i(mytag,("posle property deffered"))
                    insertNewsIntoDatabase(listResult)
                    _showProgressBar.value=false

                } catch (e: Exception) {
                    Log.i(mytag,("greska"))
                    val responseError="Failure"+e.message
                    Log.i(mytag,("$responseError"))
                    _networkError.value = true
                    // proba dok ne proradi server
                    insertNewsIntoDatabase(getNewsItemArray())
                    _showProgressBar.value=false
                    _swiperefreshfinished.value=true
                }
            }
        }else{
            Log.i(mytag,("no internet"))
           // _noInternet.value=true

        }

    }

    private fun insertNewsIntoDatabase(list:List<NetworkNewsItem>){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.clearNewsTable()
                database.insertNews(convertNetworkToDatabaseNewsItem(list))
            }
        }

    }

    fun setSwiperefreshedfinishedToFalse(){
        _swiperefreshfinished.value=false
    }


    fun noInternetSnackBarShown(){
        _noInternet.value=false
    }


    fun fetchNewsWithID(newsid:Int){
        _newsToBeOpenedID.value=newsid

    }

    fun resetNewsID() {
            _newsToBeOpenedID.value = -1
        }

    override fun onCleared() {
            super.onCleared()
            //viewModelJob.cancel()
    }

}