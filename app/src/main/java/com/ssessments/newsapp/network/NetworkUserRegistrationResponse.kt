package com.ssessments.newsapp.network


import com.squareup.moshi.Json

data class NetworkUserRegistrationResponse (

    @Json(name="status")
    var status:String,

    @Json(name="message")
    var message:String

)