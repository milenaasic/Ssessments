package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkNotificatiosObject(

    @Json(name="token")
    var token:String,

    @Json(name="sea")
    var sea:Boolean,

    @Json(name="china")
    var china: Boolean,

    @Json(name="indonesia")
    var indonesia : Boolean,

    @Json(name="malaysia")
    var malaysia : Boolean,

    @Json(name="vietnam")
    var vietnam : Boolean,

    @Json(name="india")
    var india : Boolean,

    @Json(name="pe")
    var pe: Boolean,

    @Json(name="pp")
    var pp: Boolean,

    @Json(name="pvc")
    var pvc: Boolean,

    @Json(name="pet")
    var pet: Boolean,

    @Json(name="styrenics")
    var styrenics: Boolean,

    @Json(name="daily")
    var daily: Boolean,

    @Json(name="weekly")
    var weekly: Boolean,

    @Json(name="monthly")
    var monthly : Boolean,

    @Json(name="quarterly")
    var quarterly: Boolean,

    @Json(name="news")
    var news: Boolean,

    @Json(name="price")
    var price : Boolean,

    @Json(name="stats")
    var stats : Boolean,

    @Json(name="plant")
    var plant : Boolean

)

