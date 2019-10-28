package com.ssessments.newsapp.detail_news_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkSingleNewsItem
import com.ssessments.newsapp.network.NetworkSingleNewsRequest
import com.ssessments.newsapp.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val MYTAG="MY DETAIL NEWS MODEL"
class DetailNewsViewModel(val newsID:Int,
                          val database: NewsDatabaseDao,
                          application: Application) : AndroidViewModel(application) {


    //var user=database.getUser()

    private val _singleNewsData = MutableLiveData<NetworkSingleNewsItem>()
    val singleNewsData: LiveData<NetworkSingleNewsItem>
        get() = _singleNewsData

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _shareNews = MutableLiveData<Boolean>()
    val shareNews: LiveData<Boolean>
        get() = _shareNews

    private val _showNetworkErrorMessage = MutableLiveData<Boolean>()
    val showNetworkErrorMessage: LiveData<Boolean>
        get() = _showNetworkErrorMessage

    private val _showAuthenFailedMessage = MutableLiveData<Boolean>()
    val showAuthenFailedMessage: LiveData<Boolean>
        get() = _showAuthenFailedMessage

    private val _goToLogInPage = MutableLiveData<Boolean>()
    val goToLogInPage: LiveData<Boolean>
        get() = _goToLogInPage


    init {
        _showProgressBar.value=true
        getDetailNews()
    }

    fun getDetailNews(){

        viewModelScope.launch {
            val user=database.getUserNoLiveData()
            val token=user.token
            Log.i(MYTAG," token detail news je ${token}")

            var defferedSingleNewsItem=NewsApi.retrofitService.postSingleNews(NetworkSingleNewsRequest(token,newsID))
            try {
                var result:NetworkSingleNewsItem=defferedSingleNewsItem.await()
                _singleNewsData.value=result
                _showProgressBar.value=false

            }catch (e:Exception){
                val responseError = "Failure " + e.message
                _showProgressBar.value=false
                _showNetworkErrorMessage.value = true
                //dok ne proradi server
                val fakeData=MyDetailNewsFakeData()
                _singleNewsData.value=NetworkSingleNewsItem(15,fakeData.title,fakeData.body,fakeData.TAGS,fakeData.time,
                    fakeData.author,"neki url", "imageurl")
            }
        }
    }


    fun shareItemMenuClicked(){
        _shareNews.value=true
    }

    fun shareActionComplete(){
        _shareNews.value=false
    }

    fun stopShowingProgressBar(){
        _showProgressBar.value=false
    }
}

