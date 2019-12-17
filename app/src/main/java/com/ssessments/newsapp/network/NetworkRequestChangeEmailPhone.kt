package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkRequestChangeEmailPhone (

    @Json(name="token")
    var token:String,

    @Json(name="email")
    var email:String="",

    @Json(name="phone")
    var phone:String=""


)