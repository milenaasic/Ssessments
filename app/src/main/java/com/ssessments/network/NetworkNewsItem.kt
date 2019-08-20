package com.ssessments.network

import com.squareup.moshi.Json

data class NetworkNewsItem (

    @Json(name="newsID")
    var newsID:Int,

    @Json(name="title")
    var title:String,

    @Json(name="tags")
    var tags:String,

    @Json(name="date_time")
    var date_time:String,

    @Json(name="access")
    var access:String

)