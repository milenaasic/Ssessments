package com.ssessments.newsapp.network


import com.squareup.moshi.Json

data class NetworkNewsListResponseWrapper (

    @Json(name="count")
    var count:Int,

    @Json(name="rows")
    var rows:Array<NetworkNewsItem>

)