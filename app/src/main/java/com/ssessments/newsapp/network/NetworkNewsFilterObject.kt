package com.ssessments.newsapp.network

import com.squareup.moshi.Json


data class NetworkNewsFilterObject (

    @Json(name="token")
    var token:String,

    @Json(name="markets")
    var markets:Array<String>,

    @Json(name="products")
    var products:Array<String>,

    @Json(name="ssessments")
    var ssessments: Array<String>,

    @Json(name="language")
    var language:String,

    @Json(name="dateFrom")
    var dateFrom:String,

    @Json(name="dateTo")
    var dateTo:String

)