package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkSinglePreference (

    @Json(name = "preference_key")
    var preferenceKey:String,

    @Json(name="preference_value")
    var preferenceValue:Boolean

)