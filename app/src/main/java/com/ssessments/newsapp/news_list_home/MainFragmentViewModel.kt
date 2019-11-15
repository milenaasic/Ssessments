package com.ssessments.newsapp.news_list_home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.newsapp.data.fakeNetworkNewFilterOBject
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.*
import kotlinx.coroutines.*


private const val mytag="MY_MAINFRAGM_ViewModel"
class MainFragmentViewModel(
                            val database:NewsDatabaseDao,
                            application:Application): AndroidViewModel(application) {


    //promenljiva koja sadrzi newsListu iz baze
    val newsList=database.getAllNews()

    //current filter za swip to refresh i za init
    val currentFilter=database.getCurrentFilterLiveData()

    //user za network call
    val myUser=database.getUser()

    //poseldnji filter koji je main fragment koristio pre destroy
    private var lastFilterUsedByMainFragment:CurrentFilter?= null

    private val _swiperefreshfinished = MutableLiveData<Boolean>()
    val swiperefreshfinished: LiveData<Boolean>
        get() = _swiperefreshfinished

    private val _showToastnoInternet = MutableLiveData<Boolean>()
    val showToastnoInternet: LiveData<Boolean>
        get() = _showToastnoInternet

    private val _showToastNetworkError = MutableLiveData<Boolean>()
    val showToastNetworkError: LiveData<Boolean>
        get() = _showToastNetworkError

    private val _showToastAuthentificationFailed = MutableLiveData<Boolean>()
    val showToastAuthentificationFailed : LiveData<Boolean>
        get() = _showToastAuthentificationFailed

    private val _goToLogInPage = MutableLiveData<Boolean>()
    val goToLogInPage : LiveData<Boolean>
        get() = _goToLogInPage

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _newsToBeOpenedID = MutableLiveData<Int>()
    val newsToBeOpenedID: LiveData<Int>
        get() = _newsToBeOpenedID

    init {
        _newsToBeOpenedID.value = -1
        isOnlineCheck()
        _showProgressBar.value=true
    }

    fun isOnlineCheck(){
        if(!isOnline(getApplication())){
            _showToastnoInternet.value=true
        }
    }

   fun getFilteredNewsListFromServer(filter: NetworkNewsFilterObject,initializedFromSwipeRefresh:Boolean) {

       if (!initializedFromSwipeRefresh) _showProgressBar.value = true
       Log.i(mytag, ("getFiltered news from server $filter"))

       viewModelScope.launch {
           var getDeferred = NewsApi.retrofitService.postFilteredNewsList(filter)

           try {

               var resultList = getDeferred.await()

               Log.i(mytag, "result get fileters list body je $resultList")

               if (resultList.isEmpty()) insertNewsIntoDatabase(NO_RESULT_NETWORK_NEWS_LIST)
                else insertNewsIntoDatabase(resultList)

               if (!initializedFromSwipeRefresh) _showProgressBar.value = false
               else _swiperefreshfinished.value = true

           } catch (e: Exception) {

               val responseMessage:String?=e.message

               Log.i(mytag, ("odgovor od servera je $responseMessage"))

               if (!initializedFromSwipeRefresh) _showProgressBar.value = false
               else _swiperefreshfinished.value = true

               if(responseMessage!=null) {

                   if (responseMessage.contains("401")) {
                       clearUsernameAndPassword()
                       _showToastAuthentificationFailed.value = true
                       _goToLogInPage.value = true
                   } else {
                       _showToastNetworkError.value = true

                   }
               }

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

    fun clearUsernameAndPassword(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user=database.getUserNoLiveData()
                database.updateUser(UserData(firebaseID =user.firebaseID ))
                Log.i(mytag, "claer username and password user table}")
                val a=database.getUser()
                Log.i(mytag,"posle username and password getUser daje ${a.value?.username}")
            }
        }

    }

    fun getLastFilterUsedByMainFragment():CurrentFilter?{
        return lastFilterUsedByMainFragment

    }
    fun setLastFilterUsedByMainFragment(filter:CurrentFilter){
        lastFilterUsedByMainFragment=filter
    }

    fun setSwiperefreshedfinishedToFalse(){
        _swiperefreshfinished.value=false
    }


    fun noInternetSnackBarShown(){
        _showToastnoInternet.value=false
    }

    fun networkErrorSnackBarShown(){
        _showToastNetworkError.value=false
    }

    fun authFailedSnackBarShown(){
        _showToastAuthentificationFailed.value=false
    }

    fun goToLogInFinished(){
        _goToLogInPage.value=false
    }


    fun fetchNewsWithID(newsid:Int){
        _newsToBeOpenedID.value=newsid

    }

    fun resetNewsID() {
            _newsToBeOpenedID.value = -1
        }

    fun hideProgressBar(){
        _showProgressBar.value=false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(mytag,"main grafment view model on cleared")
    }

}