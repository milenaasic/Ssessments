package com.ssessments.newsapp.network


import com.squareup.moshi.Json

data class NetworkPreferencesObject(

    @Json(name="token")
    var token:String,

    @Json(name="preferences")
    var preferenceList:Array<NetworkSinglePreference>


)

