package com.ssessments.detail_news_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.database.NewsDatabaseDao

class DetailNewsViewModelFactory(val newsID:Int,
                                 val database: NewsDatabaseDao,
                                 val application: Application
):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailNewsViewModel::class.java)) {
            return DetailNewsViewModel(newsID,database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}