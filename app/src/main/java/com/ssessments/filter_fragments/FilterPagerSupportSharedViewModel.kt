package com.ssessments.filter_fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ssessments.database.FilterItem
import com.ssessments.database.NewsDatabase
import com.ssessments.database.NewsDatabaseDao

class FilterPagerSupportSharedViewModel(application: Application): AndroidViewModel(application) {

    //val dao: NewsDatabaseDao = NewsDatabase.getInstance(application).newsDatabaseDao

    /*init {

        dao.insertFilter(FilterItem(1,"moj Filter","SEA","PE","Daily","10.3.2009","4.12.2013","english"))
        dao.insertFilter(FilterItem(2,"moj Filter","SEA","PE","Daily","10.3.2009","4.12.2013","english"))

    }*/
    fun saveFilter(item: FilterItem){
        // dao.insertFilter(item)

    }

    fun applyFilterButton(item: FilterItem){

        //ovde se ide nazad u mainfragment sa argumentima za filter koji se salju

    }
}