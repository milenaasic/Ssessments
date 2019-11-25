package com.ssessments.newsapp.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.ssessments.newsapp.utilities.Language
import com.ssessments.newsapp.utilities.NO_DATE_SELECTED_VALUE


data class NetworkNewsFilterObject (

 @Json(name="token")
   var token:String,

 @Json(name="page")
   var page:Int=1,

 @Json(name="size")
   var size:Int=1000,

 @Json(name="sort")
   var sort:String="date-desc",

 @Json(name="markets")
    var markets: Array<String>,

 @Json(name="products")
    var products:Array<String>,

 @Json(name="ssessments")
    var ssessments: Array<String>,

 @Json(name="language")
    var language:String="eng",

 @Json(name="date_from")
    var dateFrom:String=NO_DATE_SELECTED_VALUE,

 @Json(name="date_to")
    var dateTo:String=NO_DATE_SELECTED_VALUE

)