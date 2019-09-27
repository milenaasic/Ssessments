package com.ssessments.newsapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.newsapp.database.NewsDatabaseDao

class MainActivityViewModelFactory(
    val database: NewsDatabaseDao,
    val application: Application):ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
