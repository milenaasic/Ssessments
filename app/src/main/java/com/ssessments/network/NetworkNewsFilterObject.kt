package com.ssessments.network

import com.squareup.moshi.Json
import com.ssessments.utilities.Language
import com.ssessments.utilities.Markets
import com.ssessments.utilities.Products
import com.ssessments.utilities.Ssessments


data class NetworkNewsFilterObject (

    @Json(name="markets")
    var markets:Array<String>?= arrayOf(Markets.ALL.value),

    @Json(name="products")
    var products:Array<String>?= arrayOf(Products.ALL.value),

    @Json(name="ssessments")
    var ssessments: Array<String>?= arrayOf(Ssessments.ALL.value),

    @Json(name="language")
    var language:String=Language.ENGLISH.value,

    @Json(name="dateFrom")
    var dateFrom:String?=null,

    @Json(name="dateTo")
    var dateTo:String?=null

)