package com.ssessments.newsapp.network

import com.squareup.moshi.Json

data class NetworkSingleNewsItem (

    @Json(name="news_id")
    var newsID:Int,

    @Json(name="title")
    var title:String,

    @Json(name="body")
    var body:String,

    @Json(name="tags")
    var tags:Array<String>,

    @Json(name="date_time")
    var dateTime:String,

    @Json(name="author")
    var author:String,

    @Json(name="news_url")
    var newsurl:String?="www.ssessments.com",

    @Json(name="image_src")
    var imagesrc:String?



)
