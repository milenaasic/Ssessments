package com.ssessments.detail_news_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.database.UserData
import com.ssessments.network.NetworkSingleNewsItem
import com.ssessments.network.NewsApi
import com.ssessments.utilities.EMPTY_TOKEN
import kotlinx.coroutines.launch
import java.util.*


private const val MYTAG="MY DETAIL NEWS MODEL"
class DetailNewsViewModel(val newsID:Int,
                          val database: NewsDatabaseDao,
                          application: Application) : AndroidViewModel(application) {


    //TODO napravi progress bar da se vidi dok se ucitava
    var user=database.getUser()


    private val _singleNewsData = MutableLiveData<NetworkSingleNewsItem>()
    val singleNewsData: LiveData<NetworkSingleNewsItem>
        get() = _singleNewsData

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar


    init {
        _showProgressBar.value=true
    }

    fun getDetailNews(token:String){

        viewModelScope.launch {

            Log.i(MYTAG," token je ${token}")

            var defferedSingleNewsItem=NewsApi.retrofitService.postSingleNews(newsID,token)
            try {
                //result moze da bude u bodiju da nema pristupa datoj vesti ostalo prikazem i title i author i...
                var result:NetworkSingleNewsItem=defferedSingleNewsItem.await()
                _singleNewsData.value=result
                _showProgressBar.value=false

            }catch (e:Exception){
                //TODO prikazi toast something went wrong
                val fakeData=MyDetailNewsFakeData()
                _singleNewsData.value=NetworkSingleNewsItem(15,fakeData.title,fakeData.body,fakeData.TAGS,fakeData.time,
                    fakeData.author,"neki url")
                _showProgressBar.value=false


            }

        }
    }


    fun stopShowingProgressBar(){
        _showProgressBar.value=false
    }
}

