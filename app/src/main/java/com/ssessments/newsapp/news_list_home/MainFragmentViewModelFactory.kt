package com.ssessments.newsapp.news_list_home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.newsapp.database.NewsDatabaseDao

class MainFragmentViewModelFactory (
    val database: NewsDatabaseDao,
    val application: Application
): ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            return MainFragmentViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}