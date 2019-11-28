package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.database.PredefinedFilter
import com.ssessments.newsapp.utilities.DATE_SELECT_TEXT
import com.ssessments.newsapp.utilities.NO_DATE_SELECTED_VALUE
import com.ssessments.newsapp.utilities.dateFormatMySQL
import com.ssessments.newsapp.utilities.dateFormatNoHours
import java.util.*


@BindingAdapter("filter_name_string")
fun setTitle(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.filterName
    }
}

@BindingAdapter("region_string")
fun setRegion(view:TextView,item:FilterItem?){
    item?.let {
        val myDateFrom: Date= dateFormatMySQL.parse(item.dateFrom)?: Date()
        val myDateTo:Date= dateFormatMySQL.parse(item.dateTo)?:Date()
        view.text="#${item.market}, ${item.product}, ${ item.ssessment}, ${item.language}, " +
                "From:${if(item.dateFrom!= NO_DATE_SELECTED_VALUE) dateFormatNoHours.format(myDateFrom) else "No date,"} " +
                "To:${if(item.dateTo!= NO_DATE_SELECTED_VALUE) dateFormatNoHours.format(myDateTo) else "No date"}"
    }
}

@BindingAdapter("predefined_filter_name_string")
fun setTitle(view:TextView,item:PredefinedFilter?){
    item?.let {
        view.text=item.filterName
    }
}



@BindingAdapter("filter_string")
fun setFilterText(view:TextView,item:PredefinedFilter?){
    item?.let {
        view.text="#${item.market}, ${item.product}, ${ item.ssessment}, ${item.language}, " +
                "From:${if(item.dateFrom!= DATE_SELECT_TEXT)"${item.dateFrom}," else "No date,"} To:${if(item.dateTo!= DATE_SELECT_TEXT) item.dateTo else "No date"}"
    }
}

