package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.app.Application
import androidx.lifecycle.*
import com.ssessments.newsapp.database.*
import com.ssessments.newsapp.utilities.*
import kotlinx.coroutines.*

private const val MYTAG="FILTERPAGERVIEWMODEL"

class FilterPagerSupportSharedViewModel(
    val databaseDao: NewsDatabaseDao,
    application: Application):AndroidViewModel(application) {


    //CUSTOM FILTER FRAGMENT

    val currentFilter=databaseDao.getCurrentFilterLiveData()


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


    private val _navigateToMainFragmentFromSaved = MutableLiveData<Boolean>()
    val navigateToMainFragmentFromSaved: LiveData<Boolean>
        get() = _navigateToMainFragmentFromSaved


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
        updateCurrentFilter(item)
        _navigateToMainFragment.value=true

    }

    private fun updateCurrentFilter(item:CurrentFilter){
        viewModelScope.launch {

                val f=databaseDao.getCurrentFilterWithId()
                databaseDao.updateCurrentFilter(item)
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

            _chosenFilter.value=chosenfilterItem

        }

    }

    //APPLY saved filter sacuva Current Filter i ode u main fragment
    fun applySavedFilter(item: FilterItem){

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

            _chosenPredefinedFilter.value=item

        }
    }


    fun showPredefinedEmptyListText(bool: Boolean){
        _showPredefinedEmptyList.value=bool
    }




}