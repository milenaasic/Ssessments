package com.ssessments.detail_news_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailNewsViewModelFactory(private val newsID:Int):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailNewsViewModel::class.java)) {
            return DetailNewsViewModel(newsID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}