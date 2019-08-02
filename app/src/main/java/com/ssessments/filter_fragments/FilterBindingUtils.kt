package com.ssessments.filter_fragments

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.database.FilterItem

@BindingAdapter("filter_name_string")
fun setTitle(view:TextView,item:FilterItem?){
    item?.let {
        view.text=item.filterName
    }
}

@BindingAdapter("region_string")
fun setRegion(view:TextView,item:FilterItem?){
    item?.let {
        view.text="#${item.market}, ${item.product}, ${ item.ssessment}, ${item.language}, ${item.dateFrom} - ${item.dateTo}"
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