package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkSingleNewsRequest (
    @Json(name="token")
    var token:String,

    @Json(name="news_id")
    var news_id:Int
)
