package com.ssessments.newsapp.detail_news_fragment

import android.app.Application
import androidx.lifecycle.*
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkSingleNewsItem
import com.ssessments.newsapp.network.NetworkSingleNewsRequest
import com.ssessments.newsapp.network.NewsApi
import kotlinx.coroutines.launch




class DetailNewsViewModel(val newsID:Int,
                          val database: NewsDatabaseDao,
                          application: Application) : AndroidViewModel(application) {



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

            var defferedSingleNewsItem=NewsApi.retrofitService.postSingleNews(NetworkSingleNewsRequest(token,newsID))
            try {
                var result=defferedSingleNewsItem.await()
                _singleNewsData.value=result
                _showProgressBar.value=false
            }catch (e:Exception){
                val responseMessage:String?=e.message
                _showProgressBar.value=false
                _showNetworkErrorMessage.value = true

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

