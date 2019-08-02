package com.ssessments.filter_fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    init {
       insertFilterItem()

    }

    fun insertFilterItem(){

    }


    fun saveFilter(item: FilterItem){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.insertFilter(item)
            }
        }

    }



    fun applyFilterButton(item: FilterItem){

        //ovde se ide nazad u mainfragment sa argumentima za filter koji se salju

    }
}