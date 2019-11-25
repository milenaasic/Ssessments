package com.ssessments.newsapp.network


import com.squareup.moshi.Json

class NetworkCustomSearchFilterObject(

    @Json(name="token")
    var token:String,

    @Json(name="page")
    var page:Int=1,

    @Json(name="size")
    var size:Int=1000,

    @Json(name="sort")
    var sort:String="date-desc",

    @Json(name="search_by")
    var searchBy:String

)