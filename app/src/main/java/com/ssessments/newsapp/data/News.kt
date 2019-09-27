package com.ssessments.newsapp.data

import com.ssessments.newsapp.network.NetworkNewsItem


data class NewsItem(val id:Long=12,val title:String,val tags:String,val dateTime:String, val userType:String)

data class SingleNews(val id:Long,val title:String,val body:String,val tags:String,val dateTime:String)

fun getNewsItemArray(): List<NetworkNewsItem> {
    return listOf(

    NetworkNewsItem(1,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(2,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(22,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(3,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(4,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(5,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(6,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(7,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(8,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(9,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "PVC, Weekly", "Friday, 31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(10,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE, #China",
            " 4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(11,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(12,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(13,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(14,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily"," 31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(15,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(16,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(17,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(18,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(19,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(20,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(21,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(22,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(23,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(24,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "PVC, Weekly", "Friday, 31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(25,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE, #China",
        " 4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(26,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NetworkNewsItem(27,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NetworkNewsItem(28,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
        NetworkNewsItem(29,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily"," 31 May 2019, 15 : 37","Free")
    )

}
