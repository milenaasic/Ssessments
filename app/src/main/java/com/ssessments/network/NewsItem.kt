package com.ssessments.network

import com.squareup.moshi.Json

data class NetworkNewsItem (

    @Json(name="newsID")
    var newsID:Long=0L,

    @Json(name="title")
    var title:String="title",

    @Json(name="tags")
    var tags:String="tags",

    @Json(name="date_time")
    var date_time:String="date",

    @Json(name="access")
    var access:String="access"

)