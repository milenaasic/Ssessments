package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkSingleNewsItem (

    @Json(name="newsID")
    var newsID:Int,

    @Json(name="title")
    var title:String,

    @Json(name="body")
    var body:String,

    @Json(name="tags")
    var tags:String,

    @Json(name="datetime")
    var dateTime:String,

    @Json(name="author")
    var author:String,

    @Json(name="newsurl")
    var newsurl:String,

    @Json(name="imagesrc")
    var imagesrc:String?



)
