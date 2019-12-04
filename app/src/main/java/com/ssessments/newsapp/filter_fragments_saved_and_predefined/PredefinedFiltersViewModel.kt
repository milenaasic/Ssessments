package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.database.PredefinedFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PredefinedFiltersViewModel (
        val databaseDao: NewsDatabaseDao, application: Application
     ): AndroidViewModel(application) {


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

    fun navigationToMainFragmentFromPredefinedFinished(){
        _navigateToMainFragmentFromPredefined.value=false
    }


}