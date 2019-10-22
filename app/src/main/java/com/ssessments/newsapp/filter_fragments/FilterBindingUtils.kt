package com.ssessments.newsapp.filter_fragments

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.database.PredefinedFilter
import com.ssessments.newsapp.utilities.DATE_SELECT_TEXT
import com.ssessments.newsapp.utilities.NO_DATE_SELECTED_VALUE
import com.ssessments.newsapp.utilities.dateFormatMySQL
import com.ssessments.newsapp.utilities.dateFormatNoHours
import java.util.*

//saved filters rec view
@BindingAdapter("filter_name_string")
fun setTitle(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.filterName
    }
}
//saved filters rec view
@BindingAdapter("region_string")
fun setRegion(view:TextView,item:FilterItem?){
    item?.let {
        val myDateFrom: Date= dateFormatMySQL.parse(item.dateFrom)
        val myDateTo:Date= dateFormatMySQL.parse(item.dateTo)
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

/*@BindingAdapter("product_string")
fun setProduct(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.product
    }
}

@BindingAdapter("ssessment_string")
fun setSsessment(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.ssessment
        }
}

@BindingAdapter("date_from_string")
fun setDateFrom(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.dateFrom
    }
}

@BindingAdapter("date_to_string")
fun setDateTo(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.dateTo
    }
}

@BindingAdapter("language_string")
fun setLanguage(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.language
    }
}*/