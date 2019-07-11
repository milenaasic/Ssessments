package com.ssessments.news_list_home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssessments.data.NewsItem
import com.ssessments.data.getNewsItemArray

class NewsListHome_ViewModel: ViewModel() {

    var newsList=MutableLiveData<List<NewsItem>>()

    init{
        newsList.setValue(getNewsItemArray())
    }
}