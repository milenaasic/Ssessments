package com.ssessments.news_list_home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.network.NetworkNewsItem

@BindingAdapter("titleString")
fun setNewsTitle(view:TextView,item:NetworkNewsItem?){
    item?.let {
        view.text=item.title
    }
}

@BindingAdapter("dateTimeString")
fun TextView.setNewsDate(item:NetworkNewsItem?){
    item?.let {
        text=item.date_time
    }
}

@BindingAdapter("tagsString")
fun TextView.setTags(item:NetworkNewsItem?){
    item?.let {
        text=item.tags
    }
}

@BindingAdapter("userTypeString")
fun TextView.setUserType(item:NetworkNewsItem?){
    item?.let {
        text=item.access
    }
}