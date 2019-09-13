package com.ssessments.filter_fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.data.getNewsItemArray
import com.ssessments.database.CurrentFilter
import com.ssessments.database.FilterItem
import com.ssessments.database.NewsDatabase
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.network.NetworkNewsItem
import com.ssessments.network.NewsAPIService
import com.ssessments.network.NewsApi
import com.ssessments.utilities.convertCurrentFilterToNetworkNewsFilterObject
import com.ssessments.utilities.convertNetworkToDatabaseNewsItem

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MYTAG="FILTERPAGERVIEWMODEL"
class FilterPagerSupportSharedViewModel(
    val databaseDao: NewsDatabaseDao,
    application: Application):AndroidViewModel(application) {

    //FILTER BY FRAGMENT
    //current filter da se prikaza u filter by fragment-u
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



    //filteri za saved filters listu
    val filters=databaseDao.getAllFilters()


    val showEmptyList=MutableLiveData<Boolean>()


    init {
        showEmptyList.value=false
        _showProgressBar.value=false

    }


    fun saveFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.insertFilter(item)
            }
        }

    }

    fun deleteFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.deleteFilterItem(item)
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
                _showProgressBar.value=false
                _navigateToMainFragment.value=true

            } catch (e: Exception) {
                val responseError="Failure"+e.message
                Log.i(MYTAG,("$responseError"))
                _showProgressBar.value=false
                _networkError.value = true

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


    fun fetchFilterWithID(id:Long){

        /*viewModelScope.launch {
            var deferredfilterItem=databaseDao.getChosenFilter(id)
            try{
                withContext(Dispatchers.IO) {
                chosenFilter=databaseDao.getChosenFilter(id)
                Log.i(MYTAG,"chosen filter iz scope-a je $chosenFilter")
            }
        }

        Log.i(MYTAG,"chosen filter je $chosenFilter")
*/

    }

    fun fetchFilterOnBacground(id:Long):FilterItem{
        viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val filter:FilterItem= databaseDao.getChosenFilter(id)
                    Log.i(MYTAG,"chosen filter iz scope-a je $filter")

            }
    }

    //za saved filters fragment
    fun showEmptyListText(bool: Boolean){
        showEmptyList.value=bool
    }

    // za filter by fragment
    fun navigationToMainFragmentFinished(){
    _navigateToMainFragment.value=false
    }

    fun networkErrorMessageShown(){
    _networkError.value=false
    }


}