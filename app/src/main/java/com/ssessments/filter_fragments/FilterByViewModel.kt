package com.ssessments.filter_fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.ssessments.database.FilterItem
import com.ssessments.database.NewsDatabase
import com.ssessments.database.NewsDatabaseDao

class FilterByViewModel(application: Application) : AndroidViewModel(application) {

    val dao:NewsDatabaseDao=NewsDatabase.getInstance(application).newsDatabaseDao

    fun saveFilter(item:FilterItem){
        dao.insertFilter(item)

    }

    fun applyFilterButton(item: FilterItem){

        //ovde se ide nazad u mainfragment sa argumentima za filter koji se salju

    }



}
