package com.ssessments.detail_news_fragment

const val TITLE="German June Inflation Accelerated, Still Below ECB Target"
const val TIME="Friday, 12 July 2019, 17 : 12"
const val AUTHOR="Nia Widi"
const val TAGSS="Free News, Free News-EN"
const val BODY="On Thursday, the German Federal Statistics Office showed that the country’s annual inflation in June went up to 1.5%. \n" +
        "\n The number is still below the European Central Bank’s Eurozone inflation target of 2%, but up from the preliminary inflation data released at the end of June.\n" +
        "\n" +
        "Meanwhile, the EU-harmonized inflation rose by 0.3% on the month, revised from 0.1%.\n"

data class MyDetailNewsFakeData(var title:String= TITLE, var time:String= TIME, var author:String= AUTHOR, var TAGS:String= TAGSS, var body:String= BODY)