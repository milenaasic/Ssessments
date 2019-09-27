package com.ssessments.newsapp.filter_fragments

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.newsapp.database.NewsDatabaseDao

class FilterPagerSupportSharedViewModelFactory(
    val database:NewsDatabaseDao,
     val application:Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterPagerSupportSharedViewModel::class.java)) {
            return FilterPagerSupportSharedViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
