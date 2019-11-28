package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.utilities.convertFilterItemFromDatabaseToCurrentFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
private const val Tag_SavedFragment="MySavedFiltersFragment"
class SavedFiltersViewModel(
    val databaseDao:NewsDatabaseDao,application:Application):AndroidViewModel(application) {

    val loggedInUser=databaseDao.getUser()

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
        Log.i(Tag_SavedFragment,"apply saved filter je $item")
        updateCurrentFilter(convertFilterItemFromDatabaseToCurrentFilter(item))
        _navigateToMainFragmentFromSaved.value=true

    }

    private fun updateCurrentFilter(item: CurrentFilter){
        viewModelScope.launch {

            val f=databaseDao.getCurrentFilterWithId()
            databaseDao.updateCurrentFilter(item)
        }

    }


    //SAVED FILTERS FRAGMENT
    fun showEmptyListText(bool: Boolean){
        _showEmptyList.value=bool
    }

    fun navigationToMainFragmentFromSavedFinished(){
        _navigateToMainFragmentFromSaved.value=false
    }


}