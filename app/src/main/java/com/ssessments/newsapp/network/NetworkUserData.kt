package com.ssessments.newsapp.network


import com.squareup.moshi.Json

data class NetworkUserData (

    @Json(name="email")
    var username:String,

    @Json(name="password")
    var password:String,

    @Json(name="firebase_id")
    var firebaseId:String

)


