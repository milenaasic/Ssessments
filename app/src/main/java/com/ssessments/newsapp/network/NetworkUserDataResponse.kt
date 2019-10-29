package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkUserDataResponse (

    @Json(name="status")
    var status:String,

    @Json(name="token")
    var token:String

)