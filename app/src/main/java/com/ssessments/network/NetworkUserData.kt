package com.ssessments.network

import com.squareup.moshi.Json

data class NetworkUserData (

    @Json(name="username")
    var username:String,

    @Json(name="password")
    var password:String,

    @Json(name="token")
    var token:String?
)


