package com.ssessments.newsapp.network


import com.squareup.moshi.Json

data class NetworkForgotPasswordRequest (

    @Json(name="email")
    var email:String


)
