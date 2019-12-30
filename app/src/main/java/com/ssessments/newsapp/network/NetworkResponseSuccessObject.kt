package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkResponseSuccessObject (

    @Json(name="status")
    var status:String,

    @Json(name="message")
    var message:String

)

