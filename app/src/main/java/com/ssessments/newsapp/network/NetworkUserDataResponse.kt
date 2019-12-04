package com.ssessments.newsapp.network


import com.squareup.moshi.Json

data class NetworkUserDataResponse (

    @Json(name="token")
    var token:String,

    @Json(name="first_name")
    var firstName:String,

    @Json(name="last_name")
    var lastName:String,

    @Json(name="account_type")
    var accountType:String,

    @Json(name="email")
    var email:String,

    @Json(name="mobile_phone")
    var mobilePhone:String,

    @Json(name="company")
    var company:String,

    @Json(name="country")
    var country:String

)