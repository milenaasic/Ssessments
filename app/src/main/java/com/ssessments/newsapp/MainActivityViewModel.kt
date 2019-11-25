package com.ssessments.newsapp

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.*
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


    init {
        initializeUser()
        initializeCurrentFilterTable()
        initializeNewsListWithNoResultRow()
        getNotificationPreferencesFromServer()
        _swipeRefreshEnabled.value=true
    }

    private fun getNotificationPreferencesFromServer() {

        viewModelScope.launch {

            val myuser=database.getUserNoLiveData()

            if(!(myuser==null || myuser.token== EMPTY_TOKEN)){

                var getDeferred = NewsApi.retrofitService.getNotificationPreferencesFromServer(
                    NetworkRequestGetNotifPref(myuser.token)
                )

                try {
                    var resultList = getDeferred.await()
                    Log.i(MY_TAG, ("result je :${resultList}"))
                    if(!(resultList.isNullOrEmpty())) setUserNotificationPreferences(resultList)

                } catch (e: Exception) {
                    val responseMessage:String?=e.message
                    Log.i(MY_TAG, ("get notification pref from server: $responseMessage"))

                }
            }

        }
    }

    fun setUserNotificationPreferences(resultList: List<NetworkSinglePreference>) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val defSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication())
                val editor: SharedPreferences.Editor = defSharedPref.edit()

                loop@
                for(value in resultList) {

                    for (index in 0..Markets.values().size - 2) {
                        val s: String = Markets.values()[index + 1].toString().toLowerCase()
                        if (value.preferenceKey.equals(s)) {
                            editor.putBoolean(s, value.preferenceValue)
                            continue@loop
                        }
                    }

                    for (index in 0..Products.values().size - 2) {
                        val s: String = Products.values()[index + 1].toString().toLowerCase()
                        if (value.preferenceKey.equals(s)) {
                            editor.putBoolean(s, value.preferenceValue)
                            continue@loop
                        }
                    }

                    for (index in 0..Ssessments.values().size - 2) {
                        val s: String = Ssessments.values()[index + 1].toString().toLowerCase()
                        if (value.preferenceKey.equals(s)) {
                            editor.putBoolean(s, value.preferenceValue)
                            continue@loop
                        }
                    }
                }

                editor.apply()
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

                    0->{database.insertUser(UserData())
                        Log.i(MY_TAG,"user u tabeli u init Viewmodel je ${database.getUserNoLiveData()}")}
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

    fun clearUsernameAndPassword(){
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
            withContext(Dispatchers.IO){
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
                var resultList = getDeferred.await()
                Log.i(MY_TAG, ("result je :${resultList}"))

                if(resultList.isEmpty()) insertNewsIntoDatabase(NO_RESULT_NETWORK_NEWS_LIST)
                else insertNewsIntoDatabase(resultList)

                _closeSearchWidget.value=true
                _showProgressBarMainActivity.value = false

            } catch (e: Exception) {

                _showProgressBarMainActivity.value = false

                val responseMessage:String?=e.message
                Log.i(MY_TAG, ("iz custom search: $responseMessage"))

                if(responseMessage!=null){

                    if(responseMessage.contains("401")) {

                        clearUsernameAndPassword()
                        _showAuthentificationFailedMessage.value = true
                        _closeSearchWidget.value=true
                        _goToLogInPage.value=true

                    }else  _networkErrorMainActivity.value = true

                }else  _networkErrorMainActivity.value = true

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


    fun sendNotificationPreferencesToServer(entries:MutableMap<String,*>){

        /*val singlePreferencesArray2 = convertMutableListToSinglePreferencesArray(entries)
        Log.i(MY_TAG, "send Notif notif object array ${singlePreferencesArray2.size}")
        Log.i(MY_TAG, "send Notif notif object array niz ${singlePreferencesArray2.get(0).toString()}")
        Log.i(MY_TAG, "send Notif notif object array niz ${singlePreferencesArray2.get(8).toString()}")*/

        val mytoken=loggedInUser.value?.token ?: EMPTY_TOKEN
        Log.i(MY_TAG, "sendNotif $mytoken")

        if(mytoken!=null && mytoken!= EMPTY_TOKEN) {

            Log.i(MY_TAG, "sendNotif $mytoken")

            viewModelScope.launch {

                val singlePreferencesArray = convertMutableListToSinglePreferencesArray(entries)
                Log.i(MY_TAG, "network pref object je $singlePreferencesArray")

                var getValuesDeferred = NewsApi.retrofitService.sendNotificationPreferencesToServer(
                    NetworkPreferencesObject(mytoken, arrayOf(NetworkSinglePreference("key", true)))
                )

                try {
                    var result = getValuesDeferred.await()

                } catch (e: Exception) {
                    val responseError = "Failure " + e.message
                    if (responseError.contains(HTTP_AUTH_FAILED)) {
                        Log.i(MY_TAG, "network pref object je $responseError")
                    } else {
                        Log.i(MY_TAG, "network pref object je $responseError")
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




