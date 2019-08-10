package com.ssessments.filter_fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssessments.database.FilterItem
import com.ssessments.database.NewsDatabase
import com.ssessments.database.NewsDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterPagerSupportSharedViewModel(
    val databaseDao: NewsDatabaseDao,
    application: Application):AndroidViewModel(application) {


    val filters=databaseDao.getAllFilters()

    val showEmptyList=MutableLiveData<Boolean>()


    init {
        showEmptyList.value=false

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



    fun applyFilterButton(item: FilterItem){

        //ovde se ide nazad u mainfragment sa argumentima za filter koji se salju

    }

    fun showEmptyListText(bool: Boolean){
        showEmptyList.value=bool
    }


}