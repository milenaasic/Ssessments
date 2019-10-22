package com.ssessments.newsapp.data

import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.network.NetworkNewsItem


data class NewsItem(val id:Long=12,val title:String,val tags:String,val dateTime:String, val userType:String)

data class SingleNews(val id:Long,val title:String,val body:String,val tags:String,val dateTime:String)

fun getNewsItemArray(): List<NetworkNewsItem> {
    return listOf(

    NetworkNewsItem(1,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        arrayOf("#PVC, #Weekly"), "2019-11-04 18:38:00","Premium Users"),
        NetworkNewsItem(2,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open",
            arrayOf("#News,#PE,#China"),
            "2019-11-04 18:38:00","Registered Users" ),
        NetworkNewsItem(22,"DailySSESSMENTS China PE 31st May 2019", arrayOf("#Asia,#Daily"),"2019-11-04 18:38:00","Free"),

        NetworkNewsItem(3,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            arrayOf("#PVC, #Weekly"), "2019-11-04 18:38:00","Premium Users"),
        NetworkNewsItem(4,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open",
            arrayOf("#News,#PE,#China"),
            "2019-11-04 18:38:00","Registered Users" ),
        NetworkNewsItem(5,"DailySSESSMENTS China PE 31st May 2019", arrayOf("#Asia,#Daily"),"2019-11-04 18:38:00","Free"),

        NetworkNewsItem(6,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            arrayOf("#PVC, #Weekly"), "2019-11-04 18:38:00","Premium Users"),
        NetworkNewsItem(7,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open",
            arrayOf("#News,#PE,#China"),
            "2019-11-04 18:38:00","Registered Users" ),
        NetworkNewsItem(1,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            arrayOf("#PVC, #Weekly"), "2019-11-04 18:38:00","Premium Users"),
        NetworkNewsItem(2,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open",
            arrayOf("#News,#PE,#China"),
            "2019-11-04 18:38:00","Registered Users" ),
        NetworkNewsItem(22,"DailySSESSMENTS China PE 31st May 2019", arrayOf("#Asia,#Daily"),"2019-11-04 18:38:00","Free"),

        NetworkNewsItem(3,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            arrayOf("#PVC, #Weekly"), "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(4,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open",
            arrayOf("#News,#PE,#China"),
            "2019-11-04 18:38:00","Registered Users" ),
        NetworkNewsItem(5,"DailySSESSMENTS China PE 31st May 2019", arrayOf("#Asia,#Daily"),"31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(6,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            arrayOf("#PVC, #Weekly"), "2018-02-04 18:38:00","Premium Users"),
        NetworkNewsItem(7,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open",
            arrayOf("#News,#PE,#China"),
            "2019-05-27 18:38:00","Registered Users" )

    )

}

val fakeNetworkNewFilterOBject=NetworkNewsFilterObject("12345",markets = arrayOf("SEA"),products = arrayOf("PVC"),ssessments = arrayOf("Daily"),language = "English",dateTo = "SELECT",dateFrom = "SELECT")