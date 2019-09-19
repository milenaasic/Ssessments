package com.ssessments.filter_fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.data.getNewsItemArray
import com.ssessments.database.*
import com.ssessments.network.NetworkNewsItem
import com.ssessments.network.NewsAPIService
import com.ssessments.network.NewsApi
import com.ssessments.utilities.*
import kotlinx.coroutines.*

private const val MYTAG="FILTERPAGERVIEWMODEL"
class FilterPagerSupportSharedViewModel(
    val databaseDao: NewsDatabaseDao,
    application: Application):AndroidViewModel(application) {


    //CUSTOM FILTER FRAGMENT

    val currentFilter=databaseDao.getCurrentFilter()

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val _navigateToMainFragment = MutableLiveData<Boolean>()
    val navigateToMainFragment: LiveData<Boolean>
        get() = _navigateToMainFragment


    //SAVED FILTERS FRAGMENT

    val filters=databaseDao.getAllFilters()

    private val _showEmptyList = MutableLiveData<Boolean>()
    val showEmptyList: LiveData<Boolean>
        get() = _showEmptyList

    private val _chosenFilter = MutableLiveData<FilterItem>()
    val chosenFilter: LiveData<FilterItem>
        get() = _chosenFilter

    private val _showProgressBarSavedFilters = MutableLiveData<Boolean>()
    val showProgressBarSavedFilters: LiveData<Boolean>
        get() = _showProgressBarSavedFilters

    private val _navigateToMainFragmentFromSaved = MutableLiveData<Boolean>()
    val navigateToMainFragmentFromSaved: LiveData<Boolean>
        get() = _navigateToMainFragmentFromSaved

    private val _networkErrorSavedFragment = MutableLiveData<Boolean>()
    val networkErrorSavedFragment: LiveData<Boolean>
        get() = _networkErrorSavedFragment



    //PREDEFINED FILTERS FRAGMENT

    val predefinedfilters=databaseDao.getAllPredefinedFilters()

    private val _showPredefinedEmptyList = MutableLiveData<Boolean>()
    val showPredefinedEmptyList: LiveData<Boolean>
        get() = _showPredefinedEmptyList

    private val _chosenPredefinedFilter = MutableLiveData<PredefinedFilter>()
    val chosenPredefinedFilter: LiveData<PredefinedFilter>
        get() = _chosenPredefinedFilter

    private val _showProgressBarPredefinedFilters = MutableLiveData<Boolean>()
    val showProgressBarPredefinedFilters: LiveData<Boolean>
        get() = _showProgressBarPredefinedFilters

    private val _navigateToMainFragmentFromPredefined = MutableLiveData<Boolean>()
    val navigateToMainFragmentFromPredefined: LiveData<Boolean>
        get() = _navigateToMainFragmentFromPredefined

    private val _networkErrorPredefinedFragment = MutableLiveData<Boolean>()
    val networkErrorPredefinedFragment: LiveData<Boolean>
        get() = _networkErrorPredefinedFragment


    init {
        //CUSTOM
        _showProgressBar.value=false

        //SAVED
        //_showEmptyList.value=false


    }

    //CUSTOM FILTER FRAGMENT

    fun saveFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.insertFilter(item)
            }
        }

    }

    fun applyFilter(usertoken:String,item: CurrentFilter){
        // request server za news listu prema datom filteru, ako je uspesno setuj current filter i idi u main fragment
        _showProgressBar.value=true
        Log.i("MYTAG","current filter je {$item}")

        val networkitem= convertCurrentFilterToNetworkNewsFilterObject(usertoken,item)
        Log.i("MYTAG","napravljen network filter je {$networkitem}")

        viewModelScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.postFilteredNewsList(networkitem)
            try {
                var listResult = getPropertiesDeferred.await()
                Log.i(MYTAG,("posle property deffered"))
                insertNewsIntoDatabase(listResult)
                insertCurrentFilter(item)
                _showProgressBar.value=false
                _navigateToMainFragment.value=true

            } catch (e: Exception) {
                val responseError="Failure"+e.message
                Log.i(MYTAG,("$responseError"))
                _showProgressBar.value=false
                _networkError.value = true
            // DOK NE PRORADI SERVER
                insertCurrentFilter(item)

            }
        }
    }

    private fun insertNewsIntoDatabase(list:List<NetworkNewsItem>){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                databaseDao.clearNewsTable()
                databaseDao.insertNews(convertNetworkToDatabaseNewsItem(list))
            }
        }

    }

    private fun insertCurrentFilter(item:CurrentFilter){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                databaseDao.clearCurrentFilterTable()
                databaseDao.insertCurrentFilterToDatabase(item)
            }
        }

    }

    fun navigationToMainFragmentFinished(){
        _navigateToMainFragment.value=false
    }

    fun networkErrorMessageShown(){
        _networkError.value=false
    }



    //SAVED FILTERS FRAGMENT

    fun deleteFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.deleteFilterItem(item)
            }
        }
    }

    fun fetchSavedFilterWithId(id:Long){

        viewModelScope.launch {
            var deferred = async(Dispatchers.IO) {
                databaseDao.getChosenFilterFromSavesFilters(id)
            }
            var chosenfilterItem = deferred.await()
            Log.i(MYTAG, "filter iz baze je $chosenfilterItem")
            _chosenFilter.value=chosenfilterItem


        }

    }

    fun applySavedFilter(usertoken:String,item: FilterItem){
        // request server za news listu prema datom filteru, ako je uspesno setuj current filter i idi u main fragment
        _showProgressBarSavedFilters.value=true
        Log.i("MYTAG","saved filter je {$item}")

        val networkitem= convertFilterItemFromDatabaseToNetworkNewsFilterObject(usertoken,item)
        Log.i("MYTAG","napravljen network filter je {$networkitem}")

        viewModelScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.postFilteredNewsList(networkitem)
            try {
                var listResult = getPropertiesDeferred.await()
                Log.i(MYTAG,("posle property deffered"))
                insertNewsIntoDatabase(listResult)
                insertCurrentFilter(convertFilterItemFromDatabaseToCurrentFilter(item))
                _showProgressBarSavedFilters.value=false
                _navigateToMainFragmentFromSaved.value=true

            } catch (e: Exception) {
                val responseError="Failure"+e.message
                Log.i(MYTAG,("$responseError"))
                _showProgressBar.value=false
                _networkError.value = true

                //za proveru dok ne proradi network, posle obrisi
                _navigateToMainFragmentFromSaved.value=true
                insertCurrentFilter(convertFilterItemFromDatabaseToCurrentFilter(item))

            }
        }
    }

    //SAVED FILTERS FRAGMENT
    fun showEmptyListText(bool: Boolean){
        _showEmptyList.value=bool
    }

    fun navigationToMainFragmentFromSavedFinished(){
        _navigateToMainFragmentFromSaved.value=false
    }


    // PREDEFINED FILTER FRAGMENT

    fun fetchPredefinedFilterWithId(id:Long){

        viewModelScope.launch {
            var deferred = async(Dispatchers.IO) {
                databaseDao.getChosenFilterFromPredefinedFilters(id)
            }

            var item = deferred.await()
            Log.i(MYTAG, "filter iz baze je $item")
            _chosenPredefinedFilter.value=item


        }

    }


    fun applyPredefinedFilter(usertoken:String,item: PredefinedFilter){
        // request server za news listu prema datom filteru, ako je uspesno setuj current filter i idi u main fragment
        _showProgressBarPredefinedFilters.value=true
        Log.i("MYTAG","predefined filter je {$item}")

        val networkitem= convertPredefinedFilterToNetworkNewsFilterObject(usertoken,item)
        Log.i("MYTAG","napravljen network filter je {$networkitem}")

        viewModelScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.postFilteredNewsList(networkitem)
            try {
                var listResult = getPropertiesDeferred.await()
                Log.i(MYTAG,("posle property deffered"))
                insertNewsIntoDatabase(listResult)
                insertCurrentFilter(convertPredefinedFilterToCurrentFilter(item))
                _showProgressBarPredefinedFilters.value=false
                _navigateToMainFragmentFromPredefined.value=true

            } catch (e: Exception) {
                val responseError="Failure"+e.message
                Log.i(MYTAG,("$responseError"))
                _showProgressBarPredefinedFilters.value=false
                _networkErrorPredefinedFragment.value = true

                //za proveru dok ne proradi network, posle obrisi
                _navigateToMainFragmentFromPredefined.value=true
                insertCurrentFilter(convertPredefinedFilterToCurrentFilter(item))

            }
        }
    }

    fun showPredefinedEmptyListText(bool: Boolean){
        _showPredefinedEmptyList.value=bool
    }




}