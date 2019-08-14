package com.ssessments.network

import com.squareup.moshi.Json

data class NetworkUserRegistrationData(

    @Json(name="firstname")
    var firstName:String,

    @Json(name="lastname")
    var lastName:String,

    @Json(name="e_mail")
    var e_mail:String,

    @Json(name="mobilephone")
    var mobilephone:String,

    @Json(name="company")
    var company:String,

    @Json(name="country")
    var country:String,

    @Json(name="username")
    var username:String,

    @Json(name="password")
    var password:String
)