package com.ssessments.data



data class NewsItem(val id:Long=12,val title:String,val tags:String,val dateTime:String, val userType:String)

data class SingleNews(val id:Long,val title:String,val body:String,val tags:String,val dateTime:String)

fun getNewsItemArray(): List<NewsItem> {
    return listOf(

    NewsItem(1,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NewsItem(2,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
            NewsItem(22,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NewsItem(3,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NewsItem(4,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NewsItem(5,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NewsItem(6,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NewsItem(7,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NewsItem(8,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NewsItem(9,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "PVC, Weekly", "Friday, 31 May 2019, 18 : 38","Premium Users"),
        NewsItem(10,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE, #China",
            " 4 June 2019, 16 : 05","Registered Users" ),
        NewsItem(11,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

        NewsItem(12,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
            "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
        NewsItem(13,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
            "4 June 2019, 16 : 05","Registered Users" ),
        NewsItem(14,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily"," 31 May 2019, 15 : 37","Free"),

                NewsItem(15,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
    NewsItem(16,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
    NewsItem(17,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

    NewsItem(18,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
    NewsItem(19,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
    NewsItem(20,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

    NewsItem(21,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
    NewsItem(22,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
    NewsItem(23,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

    NewsItem(24,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "PVC, Weekly", "Friday, 31 May 2019, 18 : 38","Premium Users"),
    NewsItem(25,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE, #China",
        " 4 June 2019, 16 : 05","Registered Users" ),
    NewsItem(26,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily","31 May 2019, 15 : 37","Free"),

    NewsItem(27,"Vietnamese PVC Market Players Expected To Show Resistance On Larger Price Increment Despite Several Supporting Factors",
        "#PVC, #Weekly", "31 May 2019, 18 : 38","Premium Users"),
    NewsItem(28,"Chinese Producer Adjusted PE And PP Offers Following Bearish Macroeconomic News, Room For Negotiation Remains Open","#News,#PE,#China",
        "4 June 2019, 16 : 05","Registered Users" ),
    NewsItem(29,"DailySSESSMENTS China PE 31st May 2019","#Asia,#Daily"," 31 May 2019, 15 : 37","Free")
    )

}
