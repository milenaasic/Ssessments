package com.ssessments.newsapp.news_list_home

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.newsapp.database.NewsItem
import com.ssessments.newsapp.utilities.dateStringFormatISO8601oReadableWithHours
import com.ssessments.newsapp.utilities.dateStringFormatSQlToReadableWithHours


@BindingAdapter("titleString")
fun setNewsTitle(view:TextView,item: NewsItem?){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        view.setText(Html.fromHtml(item?.title, Html.FROM_HTML_MODE_COMPACT))
    } else {
        view.setText(Html.fromHtml(item?.title))

    }

}

@BindingAdapter("dateTimeString")
fun TextView.setNewsDate(item:NewsItem?){
    item?.let {
        text= dateStringFormatISO8601oReadableWithHours(item.date_time)
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

