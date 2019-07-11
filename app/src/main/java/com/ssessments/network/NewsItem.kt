package com.ssessments.network

import com.squareup.moshi.Json

data class NewsItem (
            val id:Int,
            @Json(name="img_src") val imgSrcUrl:String,
            val title:String
)