package com.ssessments.newsapp.network


import com.squareup.moshi.Json
import com.ssessments.newsapp.utilities.Language

data class NetworkPreferencesObject(

    @Json(name="token")
    var token:String,

    @Json(name="preferences")
    var preferenceList:Array<NetworkSinglePreference>,

    @Json(name="language")
    var language:String=Language.ENGLISH.value



)

