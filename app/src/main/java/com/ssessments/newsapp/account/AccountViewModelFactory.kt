package com.ssessments.newsapp.account

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.news_list_home.MainFragmentViewModel

class AccountViewModelFactory (val database: NewsDatabaseDao,
                            val application: Application): ViewModelProvider.Factory {


    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}