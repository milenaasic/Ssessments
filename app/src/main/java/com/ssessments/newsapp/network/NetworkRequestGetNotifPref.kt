package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkRequestGetNotifPref (

@Json(name="token")
var token:String

)