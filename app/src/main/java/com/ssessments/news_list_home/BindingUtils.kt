package com.ssessments.news_list_home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.data.NewsItem

@BindingAdapter("titleString")
fun setNewsTitle(view:TextView,item:NewsItem?){
    item?.let {
        view.text=item.title
    }
}

@BindingAdapter("dateTimeString")
fun TextView.setNewsDate(item:NewsItem?){
    item?.let {
        text=item.dateTime
    }
}

@BindingAdapter("tagsString")
fun TextView.setTags(item:NewsItem?){
    item?.let {
        text=item.tags
    }
}

@BindingAdapter("userTypeString")
fun TextView.setUserType(item:NewsItem?){
    item?.let {
        text=item.userType
    }
}