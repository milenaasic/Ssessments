package com.ssessments.newsapp.filter_fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.newsapp.database.*
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.*
import kotlinx.coroutines.*

private const val MYTAG="FILTERPAGERVIEWMODEL"

class FilterPagerSupportSharedViewModel(
    val databaseDao: NewsDatabaseDao,
    application: Application):AndroidViewModel(application) {


    //CUSTOM FILTER FRAGMENT

    val currentFilter=databaseDao.getCurrentFilterLiveData()

    /*private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar*/

    /*private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError*/

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

   /* private val _showProgressBarSavedFilters = MutableLiveData<Boolean>()
    val showProgressBarSavedFilters: LiveData<Boolean>
        get() = _showProgressBarSavedFilters*/

    private val _navigateToMainFragmentFromSaved = MutableLiveData<Boolean>()
    val navigateToMainFragmentFromSaved: LiveData<Boolean>
        get() = _navigateToMainFragmentFromSaved

    /*private val _networkErrorSavedFragment = MutableLiveData<Boolean>()
    val networkErrorSavedFragment: LiveData<Boolean>
        get() = _networkErrorSavedFragment*/



    //PREDEFINED FILTERS FRAGMENT

    val predefinedfilters=databaseDao.getAllPredefinedFilters()

    private val _showPredefinedEmptyList = MutableLiveData<Boolean>()
    val showPredefinedEmptyList: LiveData<Boolean>
        get() = _showPredefinedEmptyList

    private val _chosenPredefinedFilter = MutableLiveData<PredefinedFilter>()
    val chosenPredefinedFilter: LiveData<PredefinedFilter>
        get() = _chosenPredefinedFilter

    /*private val _showProgressBarPredefinedFilters = MutableLiveData<Boolean>()
    val showProgressBarPredefinedFilters: LiveData<Boolean>
        get() = _showProgressBarPredefinedFilters*/

    private val _navigateToMainFragmentFromPredefined = MutableLiveData<Boolean>()
    val navigateToMainFragmentFromPredefined: LiveData<Boolean>
        get() = _navigateToMainFragmentFromPredefined

    /*private val _networkErrorPredefinedFragment = MutableLiveData<Boolean>()
    val networkErrorPredefinedFragment: LiveData<Boolean>
        get() = _networkErrorPredefinedFragment*/


    init {

    }

    //CUSTOM FILTER FRAGMENT

    fun saveFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.insertFilter(item)
            }
        }

    }

    //APPLY button sacuva Current Filter i ode u main fragment
    fun applyFilter(item: CurrentFilter){
        Log.i("MYTAG","current filter je {$item}")

        updateCurrentFilter(item)
        //_showProgressBar.value=false
        _navigateToMainFragment.value=true

    }

    private fun updateCurrentFilter(item:CurrentFilter){
        viewModelScope.launch {

                Log.i(MYTAG,"updateCurrentTo Databse $item")

                val n=databaseDao.getNumberOfCurrentFilters()
                Log.i(MYTAG,"broj current filtera u tabeli je $n")

                val f=databaseDao.getCurrentFilterWithId()
                Log.i(MYTAG,"filter sa id =1L  je $f")

                val fe=databaseDao.getCurrentFilterWithLanguage()
                Log.i(MYTAG,"filter sa english  je $fe")

                databaseDao.updateCurrentFilter(item)

                val n2=databaseDao.getNumberOfCurrentFilters()
                Log.i(MYTAG,"broj current filtera posle update u tabeli je $n2")

                val f3=databaseDao.getCurrentFilterWithId()
                Log.i(MYTAG,"filter sa id =1L  je $f3")

                val f4=databaseDao.getCurrentFilterWithLanguage()
                Log.i(MYTAG,"filter sa english  je $f4")

        }


    }

    fun navigationToMainFragmentFinished(){
        _navigateToMainFragment.value=false
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

    //APPLY saved filter sacuva Current Filter i ode u main fragment
    fun applySavedFilter(item: FilterItem){

        Log.i("MYTAG","saved filter je {$item}")
        updateCurrentFilter(convertFilterItemFromDatabaseToCurrentFilter(item))
        _navigateToMainFragmentFromSaved.value=true

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


    fun showPredefinedEmptyListText(bool: Boolean){
        _showPredefinedEmptyList.value=bool
    }




}