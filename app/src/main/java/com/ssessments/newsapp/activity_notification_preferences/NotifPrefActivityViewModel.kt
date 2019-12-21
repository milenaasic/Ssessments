package com.ssessments.newsapp.activity_notification_preferences

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkPreferencesObject
import com.ssessments.newsapp.network.NetworkSinglePreference
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.*
import com.ssessments.newsapp.utilities.AmericasMarkets
import com.ssessments.newsapp.utilities.AsiaPacificMarkets
import com.ssessments.newsapp.utilities.EuropeMarkets
import com.ssessments.newsapp.utilities.ICSMarkets
import com.ssessments.newsapp.utilities.MiddleEastMarkets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

private const val MY_TAG="MY_NotifPrefActViewMode"
class NotifPrefActivityViewModel(val database: NewsDatabaseDao,
                                 application:Application):AndroidViewModel(application) {

    var loggedInUser=database.getUser()


    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
    var lastSavedValues=sharedPreferences.all as Map<String,Boolean>

    private val _asiaPacificSummary = MutableLiveData<ArrayList<String>>()
    val asiaPacificSummary: LiveData<ArrayList<String>>
        get() = _asiaPacificSummary

    private val _americasSummary = MutableLiveData<ArrayList<String>>()
    val americasSummary: LiveData<ArrayList<String>>
        get() = _americasSummary

    private val _europeSummary = MutableLiveData<ArrayList<String>>()
    val europeSummary: LiveData<ArrayList<String>>
        get() = _europeSummary

    private val _icsSummary = MutableLiveData<ArrayList<String>>()
    val icsSummary: LiveData<ArrayList<String>>
        get() = _icsSummary

    private val _middleeastSummary = MutableLiveData<ArrayList<String>>()
    val middleeastSummary: LiveData<ArrayList<String>>
        get() = _middleeastSummary

    private val _plasticsSummary = MutableLiveData<ArrayList<String>>()
    val plasticsSummary: LiveData<ArrayList<String>>
        get() = _plasticsSummary

    private val _chemicalsSummary = MutableLiveData<ArrayList<String>>()
    val chemicalsSummary: LiveData<ArrayList<String>>
        get() = _chemicalsSummary

    private val _energySummary = MutableLiveData<ArrayList<String>>()
    val energySummary: LiveData<ArrayList<String>>
        get() = _energySummary

    private val _servicesSummary = MutableLiveData<ArrayList<String>>()
    val servicesSummary: LiveData<ArrayList<String>>
        get() = _servicesSummary

    private val _finishAndGoToMain = MutableLiveData<Boolean>()
    val finishAndGoToMain: LiveData<Boolean>
        get() = _finishAndGoToMain


    init {

        initializeRegionsSummaries()
        initializeProductsSummaries()
        initializeServicesSummaries()


    }

    private fun initializeServicesSummaries() {
        viewModelScope.launch {
            _servicesSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<Services>().joinToString { it.value })
            )

        }
    }

    private fun initializeProductsSummaries() {
        viewModelScope.launch {
            _plasticsSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<Plastics>().joinToString { it.value })
            )
            _chemicalsSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<Chemicals>().joinToString { it.value })
            )
            _energySummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<EnergyFeedstocks>().joinToString { it.value })
            )

        }
    }


    private fun initializeRegionsSummaries() {

        viewModelScope.launch {

            _asiaPacificSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<AsiaPacificMarkets>().joinToString { it.value })
            )
            _americasSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<AmericasMarkets>().joinToString { it.value })
            )
            _europeSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<EuropeMarkets>().joinToString { it.value })
            )
            _icsSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<ICSMarkets>().joinToString { it.value })
            )
            _middleeastSummary.value = setSummary(
                sharedPreferences,
                convertStringWithCommasToRealArray(enumValues<MiddleEastMarkets>().joinToString { it.value })
            )
        }

    }

    private fun setSummary(sharedPref: SharedPreferences, array: Array<String>): ArrayList<String> {

        val list = ArrayList<String>()
        for (i in 1..array.size - 1) {
            val currentKey =
                array[i].trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
            val bool = sharedPref.getBoolean(currentKey, true)
            Log.i(
                MY_TAG,
                "set summarz ${currentKey}, vrednost je ${sharedPref.getBoolean(currentKey, true)}"
            )
            if (bool) list.add(array[i])

        }

        return list
    }


    fun setCurrentFilterAccordingToNotifications(currentFilter: CurrentFilter) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.updateCurrentFilter(currentFilter)
            }

            Log.i(MY_TAG, "filter iz notifikacija je $currentFilter")
        }
        _finishAndGoToMain.value=true
    }

    fun setAsiaPacificSummaryFromFragment(array: Array<String>) {
            _asiaPacificSummary.value = setSummary(sharedPreferences, array)
        }

    fun setAmericasSummaryFromFragment(array: Array<String>) {
            _americasSummary.value = setSummary(sharedPreferences, array)
        }

    fun setEuropeSummaryFromFragment(array: Array<String>) {
            _europeSummary.value = setSummary(sharedPreferences, array)
        }

    fun setICSSummaryFromFragment(array: Array<String>) {
            _icsSummary.value = setSummary(sharedPreferences, array)
        }

    fun setMiddleeastSummaryFromFragment(array: Array<String>) {
            _middleeastSummary.value = setSummary(sharedPreferences, array)
    }

    fun setPlasticsSummaryFromFragment(array: Array<String>) {
            _plasticsSummary.value = setSummary(sharedPreferences, array)
    }

    fun setChemicalsSummaryFromFragment(array: Array<String>) {
            _chemicalsSummary.value = setSummary(sharedPreferences, array)
    }

    fun setEnergySummaryFromFragment(array: Array<String>) {
            _energySummary.value = setSummary(sharedPreferences, array)
    }

    fun setServicesSummaryFromFragment(array: Array<String>) {
            _servicesSummary.value = setSummary(sharedPreferences, array)
    }

    fun finishAndGoToMainOver(){
        _finishAndGoToMain.value=false
    }


    fun sendNotificationPreferencesToServer(entries:Map<String,Boolean>,languageForNotif:String){

        val mytoken=loggedInUser.value?.token ?: EMPTY_TOKEN
        Log.i(MY_TAG,"logged in user je iz send notif ${loggedInUser.value}")

        Log.i(MY_TAG, "sendNotif $mytoken")

        if(mytoken!=null && mytoken!= EMPTY_TOKEN) {

            Log.i(MY_TAG, "sendNotif $mytoken")

            viewModelScope.launch {

                val singlePreferencesArray = convertMutableListToSinglePreferencesArray(entries)
                Log.i(MY_TAG, "network pref object je $singlePreferencesArray")

                var getValuesDeferred = NewsApi.retrofitService.sendNotificationPreferencesToServer(
                    NetworkPreferencesObject(mytoken, singlePreferencesArray, language =languageForNotif ))

                try {
                    var result = getValuesDeferred.await()

                } catch (e: Exception) {
                    val responseError = "Failure " + e.message
                    if (responseError.contains(HTTP_AUTH_FAILED)) {
                        Log.i(MY_TAG,"network pref object je $responseError")
                    } else {
                        Log.i(MY_TAG, "network pref object je $responseError")
                    }
                }

            }
        }
    }


    override fun onCleared() {
        super.onCleared()

    }

}