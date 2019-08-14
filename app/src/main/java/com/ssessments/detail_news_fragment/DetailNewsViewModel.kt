package com.ssessments.detail_news_fragment

import android.util.Log
import androidx.lifecycle.ViewModel;

class DetailNewsViewModel(newsID:Int) : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.v("DETAILVIEWMODEL"," news id je $newsID")
    }
}

