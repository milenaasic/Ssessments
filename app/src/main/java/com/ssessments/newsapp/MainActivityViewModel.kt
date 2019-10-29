package com.ssessments.newsapp

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.ssessments.newsapp.data.fakeNetworkNewFilterOBject
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.NetworkCustomSearchFilterObject
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.network.NetworkNotificatiosObject
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.*
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

    private val _showAuthentificationFailedMessage = MutableLiveData<Boolean>()
    val showAuthentificationFailedMessage: LiveData<Boolean>
        get() = _showAuthentificationFailedMessage

    private val _goToLogInPage = MutableLiveData<Boolean>()
    val goToLogInPage: LiveData<Boolean>
        get() = _goToLogInPage

    private val _closeSearchWidget = MutableLiveData<Boolean>()
    val closeSearchWidget: LiveData<Boolean>
        get() = _closeSearchWidget


    private val _networkErrorMainActivity = MutableLiveData<Boolean>()
    val networkErrorMainActivity: LiveData<Boolean>
        get() = _networkErrorMainActivity

    // da li da Main Fragment iskljuci swipe refresh opciju
    // kada je custom search otvoren ne radi swipe refresh
    private val _swipeRefreshEnabled = MutableLiveData<Boolean>()
    val swipeRefreshEnabled: LiveData<Boolean>
        get() = _swipeRefreshEnabled

    /*private val _navigateUpToMainFragment = MutableLiveData<Boolean>()
    val navigateUpToMainFragment: LiveData<Boolean>
        get() = _navigateUpToMainFragment*/

    /*fun navigationToMainFragmentFinished(){
        _navigateUpToMainFragment.value=false
    }*/

    init {
        trylocalHost()
        initializeUser()
        initializeCurrentFilterTable()
        initializeNewsListWithNoResultRow()
        _swipeRefreshEnabled.value=true
    }

    private fun trylocalHost() {

        viewModelScope.launch {

            var getPropertiesDeferred = NewsApi.retrofitService.getTestValuesFromLocalServer()

            try {
                var listResult = getPropertiesDeferred.await()
               val niz=listResult.body()
                Log.i(MY_TAG, ("response je :$listResult"))
                // Log.i(MY_TAG, ("result je :${listResult.body()}"))

            } catch (e: Exception) {
                val responseError = "Failure " + e.message
                Log.i(MY_TAG, ("iz try local hostY: $responseError"))

            }
        }
    }

    private fun initializeNewsListWithNoResultRow() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i(MY_TAG, "initialize new list table")
                Log.i(MY_TAG,"broj zapisa u news listi je ${database.getNumberOfRowsInNewsListTable()}")
                when (database.getNumberOfRowsInNewsListTable()){
                    0-> database.insertNews(convertNetworkToDatabaseNewsItem(NO_RESULT_NETWORK_NEWS_LIST))
                    else->return@withContext}
            }
        }
    }

    private fun initializeUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i(MY_TAG, "initialize user")
                when (database.getNumberOfUsers()){
                    0-> database.insertUser(UserData())
                    1->return@withContext}

                Log.i(MY_TAG,"user u tabeli u init Viewmodel je ${database.getUserNoLiveData()}")

            }
        }

    }


    private fun initializeCurrentFilterTable(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i(MY_TAG, "initialize currentFilterTable")
                when (database.getNumberOfCurrentFilters()){
                0-> database.insertCurrentFilterToDatabase(CurrentFilter(market = Markets.ALL_MARKETS.value,product = Products.ALL_PRODUCTS.value,ssessment = Ssessments.ALL_SERVICES.value))
                1->return@withContext}
            }
        }

    }

    fun clearUser(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val myFirebaseid=database.getUserNoLiveData().firebaseID
                database.updateUser(UserData(username= EMPTY_USERNAME,password = EMPTY_PASSWORD,token= EMPTY_TOKEN,firebaseID = myFirebaseid))
                Log.i(MY_TAG, "claer user table}")
                val a=database.getUserNoLiveData()
                Log.i(MY_TAG,"posle clear getUser daje $a.value")
            }
        }
    }

    fun setCurrentFilterAccordingToNotifications(currentFilter:CurrentFilter){
        updateCurrentFilterInDatabase(currentFilter)

    }

    private fun updateCurrentFilterInDatabase(filter:CurrentFilter){
        viewModelScope.launch {
            with(Dispatchers.IO){
                database.updateCurrentFilter(filter)}
            Log.i(MY_TAG, "filter iz notifikacija je $filter")
        }
    }

    fun doCustomNewsSearch(searchString:String){

        _showProgressBarMainActivity.value=true

        val mytoken=loggedInUser.value?.token ?: EMPTY_TOKEN

        viewModelScope.launch {
            var getDeferred = NewsApi.retrofitService.postCustomSearchNewsList(NetworkCustomSearchFilterObject(token=mytoken,searchBy = searchString))
            try {
                var result = getDeferred.await()
                Log.i(MY_TAG, ("result je :${result.body()}"))

                if (result.body()?.count == 0) insertNewsIntoDatabase(NO_RESULT_NETWORK_NEWS_LIST)
                else {
                    val array: Array<NetworkNewsItem>? = result.body()?.rows
                    val mylist = array?.toList()
                    Log.i(MY_TAG, "ucitana i konvertovana lista je $mylist")
                    insertNewsIntoDatabase(mylist ?: NO_RESULT_NETWORK_NEWS_LIST)
                }

                 _showProgressBarMainActivity.value = false

            } catch (e: Exception) {
                val responseError = "Failure " + e.message
                Log.i(MY_TAG, ("iz custom search: $responseError"))

                when {
                    responseError.contains(HTTP_AUTH_FAILED) -> {
                                                                        clearUser()
                                                                        _showAuthentificationFailedMessage.value = true
                                                                        _closeSearchWidget.value=true
                                                                        _goToLogInPage.value=true
                                                                    }
                    else-> { _networkErrorMainActivity.value = true
                        //dok ne proradi server
                        //clearUser()
                        //_closeSearchWidget.value=true
                         //_showAuthentificationFailedMessage.value = true
                        //_goToLogInPage.value=true
                    }


                }

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


    fun sendNotificationPreferencesToServer(token:String,entries:MutableMap<String,*>){

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val networkPrefObject = convertMutableListToNetworkNotificationsObject(token, entries)
                Log.i(MY_TAG, "network pref object je $networkPrefObject")

                var getValuesDeferred = NewsApi.retrofitService.sendNotificationPreferencesToServer(networkPrefObject)

                try {
                    var result = getValuesDeferred.await()

                } catch (e: Exception) {
                    val responseError = "Failure " + e.message
                    when {
                        responseError.contains(HTTP_AUTH_FAILED) -> {
                            Log.i(MY_TAG, "network pref object je $responseError")
                        }
                        else -> {
                            Log.i(MY_TAG, "network pref object je $responseError")
                        }
                    }
                }
            }
        }

    }

    fun setSwipeRefreshEnabled(bool:Boolean){
        _swipeRefreshEnabled.value=bool
        Log.i(MY_TAG,"swipe refresh enabled je $bool")
    }


    fun authentificationFailedMessaheShown(){
        _showAuthentificationFailedMessage.value=false
    }

    fun networkErrorSnackbarShown(){
        _networkErrorMainActivity.value=false
    }


    fun goToLogInPageFinished(){
        _goToLogInPage.value=false
    }

    fun searchWidgetClosed(){
        _closeSearchWidget.value=false

    }

}




