package com.ssessments.newsapp.filter_activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.utilities.convertStringWithCommasToArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MYTAG="FilterActivityViewModel"

class FilterActivityViewModel (val database: NewsDatabaseDao,
                               application: Application): AndroidViewModel(application){


    var myloggedInUser=database.getUser()


    //CUSTOM FILETR FRAGMENT
    val mychosenFromDate:MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    val mychosenToDate:MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    private val _navigateToMainActivity = MutableLiveData<Boolean>()
    val navigateToMainActivity: LiveData<Boolean>
        get() = _navigateToMainActivity



    // MARKETS FRAGMENT
    val mychosenMarketsTags:MutableLiveData<ArrayList<String>> by lazy{
        MutableLiveData<ArrayList<String>>()
    }
    private val _navigateFromMarketsToCustomFragment = MutableLiveData<Boolean>()
    val navigateFromMarketsToCustomFragment: LiveData<Boolean>
        get() = _navigateFromMarketsToCustomFragment


    // PRODUCTS FRAGMENT
    val mychosenProductsTags:MutableLiveData<ArrayList<String>> by lazy{
        MutableLiveData<ArrayList<String>>()
    }

    private val _navigateFromProductsToCustomFragment = MutableLiveData<Boolean>()
    val navigateFromProductsToCustomFragment: LiveData<Boolean>
        get() = _navigateFromProductsToCustomFragment


    // SERVICES FRAGMENT
    val mychosenServicesTags:MutableLiveData<ArrayList<String>> by lazy{
        MutableLiveData<ArrayList<String>>()
    }

    private val _navigateFromServicesToCustomFragment = MutableLiveData<Boolean>()
    val navigateFromServicesToCustomFragment: LiveData<Boolean>
        get() = _navigateFromServicesToCustomFragment


    init {
        initilizechosenFilter()
        Log.i(MYTAG,"view modela init")
    }



    //CUSTOM FILTER FRAGMENT
    fun initilizechosenFilter(){
       viewModelScope.launch{
           val currentFilterInDB=database.getCurrentFilterWithId()
           val currentmarkets=currentFilterInDB.market
           val currentproducts=currentFilterInDB.product
           val currentservices=currentFilterInDB.ssessment
           Log.i(MYTAG,"marketsi u bazi su $currentmarkets")
           mychosenMarketsTags.value= convertStringWithCommasToArray(currentmarkets)
           mychosenProductsTags.value= convertStringWithCommasToArray(currentproducts)
           mychosenServicesTags.value= convertStringWithCommasToArray(currentservices)
           mychosenFromDate.value=currentFilterInDB.dateFrom
           mychosenToDate.value=currentFilterInDB.dateTo
       }
    }

    fun setChosenFromDate(date:String){
        mychosenFromDate.value=date
        Log.i(MYTAG,"lista chekiranih date je $date")

    }

    fun setChosenToDate(date:String){
        mychosenToDate.value=date
        Log.i(MYTAG,"lista chekiranih to date je $date")

    }

    fun saveFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database.insertFilter(item)
            }
        }
    }

    //APPLY button sacuva Current Filter i ode u main fragment
    fun applyFilter(item: CurrentFilter){
        Log.i(MYTAG,"current filter je {$item}")
        updateCurrentFilter(item)
        _navigateToMainActivity.value=true
    }

    private fun updateCurrentFilter(item: CurrentFilter){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i(MYTAG, "updateCurrentTo Databse $item")

                val n = database.getNumberOfCurrentFilters()
                Log.i(MYTAG, "broj current filtera u tabeli je $n")

                val f = database.getCurrentFilterWithId()
                Log.i(MYTAG, "filter sa id =1L  je $f")

                database.updateCurrentFilter(item)

                val n2 = database.getNumberOfCurrentFilters()
                Log.i(MYTAG, "broj current filtera posle update u tabeli je $n2")

                val f3 = database.getCurrentFilterWithId()
                Log.i(MYTAG, "filter sa id =1L  je $f3")

            }
        }
    }

    fun navigationToMainActivityFinished(){
        _navigateToMainActivity.value=false
    }




    // MARKETS FRAGMENT
    fun setChosenMarketsTags(array: ArrayList<String>){
        mychosenMarketsTags.value=array
        Log.i(MYTAG,"lista chekiranih marketsa je $array")

    }

    fun startNavigateFromMarketsToCustomFragment(){
        _navigateFromMarketsToCustomFragment.value=true
    }

    fun navigateFromMarketsToCustomFragmentFinished(){
        _navigateFromMarketsToCustomFragment.value=false
    }






    // PRODUCTS FRAGMENT
    fun setChosenProductsTags(array: ArrayList<String>){
        mychosenProductsTags.value=array
        Log.i(MYTAG,"lista chekiranih productsa je $array")

    }

    fun startNavigateFromProductsToCustomFragment(){
        _navigateFromProductsToCustomFragment.value=true
    }

    fun navigateFromProductsToCustomFragmentFinished(){
        Log.i(MYTAG,"u navigate from products tocustom ")
        _navigateFromProductsToCustomFragment.value=false
        Log.i(MYTAG,"u navigate from products tocustom  value je ${_navigateFromProductsToCustomFragment.value.toString()}")
    }





    // SERVICES FRAGMENT
    fun setChosenServicesTags(array: ArrayList<String>){
        mychosenServicesTags.value=array
        Log.i(MYTAG,"lista chekiranih Services je $array")
    }

    fun startNavigateFromServicesToCustomFragment(){
        _navigateFromServicesToCustomFragment.value=true
    }

    fun navigateFromServicesToCustomFragmentFinished(){
        _navigateFromServicesToCustomFragment.value=false
    }


}