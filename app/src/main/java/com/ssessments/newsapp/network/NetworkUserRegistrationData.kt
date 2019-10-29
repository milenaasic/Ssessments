package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkUserRegistrationData(

    @Json(name="first_name")
    var firstName:String,

    @Json(name="last_name")
    var lastName:String,

    @Json(name="email")
    var e_mail:String,

    @Json(name="mobile_phone")
    var mobilephone:String,

    @Json(name="company")
    var company:String,

    @Json(name="country")
    var country:String="",

    @Json(name="username")
    var username:String,

    @Json(name="password")
    var password:String
)