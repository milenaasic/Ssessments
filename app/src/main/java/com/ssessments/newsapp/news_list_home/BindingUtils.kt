package com.ssessments.newsapp.news_list_home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.newsapp.database.NewsItem
import com.ssessments.newsapp.utilities.dateStringFormatSQlToReadableWithHours


@BindingAdapter("titleString")
fun setNewsTitle(view:TextView,item: NewsItem?){
    item?.let {
        view.text=item.title
    }
}

@BindingAdapter("dateTimeString")
fun TextView.setNewsDate(item:NewsItem?){
    item?.let {
        text= dateStringFormatSQlToReadableWithHours(item.date_time)
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
        text=item.access
    }
}
