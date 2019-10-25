package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkNotificatiosObject(

    @Json(name="markets")
    var markets:Array<Boolean>,

    @Json(name="products")
    var products:Array<Boolean>,

    @Json(name="ssessments")
    var ssessments:Array<Boolean>

)

