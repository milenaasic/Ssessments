package com.ssessments.newsapp.detail_news_fragment

const val TITLE="German June Inflation Accelerated, Still Below ECB Target"
const val TIME="Friday, 12 July 2019, 17 : 12"
const val AUTHOR="Nia Widi"

const val BODY="<h2>Title</h2><br><p>Description here</p>"

data class MyDetailNewsFakeData(var title:String= TITLE, var time:String= TIME, var author:String= AUTHOR, var tags:Array<String>, var body:String= BODY)