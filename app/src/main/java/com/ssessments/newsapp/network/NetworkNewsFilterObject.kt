package com.ssessments.newsapp.network

import com.squareup.moshi.Json


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
    var markets:Array<String>,

    @Json(name="products")
    var products:Array<String>,

    @Json(name="ssessments")
    var ssessments: Array<String>,

    @Json(name="language")
    var language:String,

    @Json(name="date_from")
    var dateFrom:String,

    @Json(name="date_to")
    var dateTo:String

)