package com.ssessments.utilities

import android.text.TextUtils
import com.ssessments.database.NewsItem
import com.ssessments.network.NetworkNewsItem
import android.text.TextUtils.SimpleStringSplitter
import com.ssessments.database.CurrentFilter
import com.ssessments.network.NetworkNewsFilterObject


fun convertNetworkToDatabaseNewsItem(networkNewsList:List<NetworkNewsItem>):List<NewsItem>{
    var resultList= ArrayList<NewsItem>()

    for(item in networkNewsList){
        resultList.add(NewsItem(0L,item.newsID,item.title,item.tags,item.date_time,item.access))

    }
    return resultList
}

fun convertCurrentFilterToNetworkNewsFilterObject(token:String,item:CurrentFilter):NetworkNewsFilterObject{
return NetworkNewsFilterObject( token,
                                convertStringWithCommasToRealArray(item.market),
                                convertStringWithCommasToRealArray(item.product),
                                convertStringWithCommasToRealArray(item.ssessment),
                                item.language,
                                item.dateFrom,
                                item.dateTo)
}

fun convertStringWithCommasToArray(s: String):ArrayList<String>{

    val list=ArrayList<String>()
    TextUtils.SimpleStringSplitter(',').apply {
           setString(s)
           while (hasNext()){
           list.add(next())
           }
    }
    return list
}

fun convertArrayListToStringWithCommas(list:ArrayList<String>):String{
    return TextUtils.join(",",list)
}

fun convertStringWithCommasToRealArray(s: String):Array<String>{
    val list= convertStringWithCommasToArray(s)
    return list.toTypedArray()
}
