package com.ssessments.newsapp.utilities

import android.text.TextUtils
import android.util.Log
import com.ssessments.newsapp.database.NewsItem
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.database.PredefinedFilter
import com.ssessments.newsapp.network.NetworkNewsFilterObject


fun convertNetworkToDatabaseNewsItem(networkNewsList:List<NetworkNewsItem>):List<NewsItem>{
    var resultList= ArrayList<NewsItem>()

    for(item in networkNewsList){

        resultList.add(NewsItem(0L,item.newsID,item.title,TextUtils.join(",",item.tags),item.date_time,item.access))
        Log.i("konverzije","iz string array u string ${TextUtils.join(",",item.tags)}")

    }
    return resultList
}

fun convertCurrentFilterToNetworkNewsFilterObject(token:String,item:CurrentFilter):NetworkNewsFilterObject{
return NetworkNewsFilterObject( token=token,
                                markets = convertStringWithCommasToRealArray(item.market),
                                products = convertStringWithCommasToRealArray(item.product),
                                ssessments = convertStringWithCommasToRealArray(item.ssessment),
                                language = item.language,
                                dateFrom = item.dateFrom,
                                dateTo = item.dateTo)
}

fun convertFilterItemFromDatabaseToNetworkNewsFilterObject(token:String,item:FilterItem):NetworkNewsFilterObject{
    return NetworkNewsFilterObject( token=token,
                                    markets = convertStringWithCommasToRealArray(item.market),
                                    products = convertStringWithCommasToRealArray(item.product),
                                    ssessments = convertStringWithCommasToRealArray(item.ssessment),
                                    language = item.language,
                                    dateFrom = item.dateFrom,
                                    dateTo = item.dateTo)
}

fun convertFilterItemFromDatabaseToCurrentFilter(item:FilterItem):CurrentFilter{
    return CurrentFilter(
        market = item.market,
        product = item.product,
        ssessment = item.ssessment,
       language =  item.language,
        dateFrom = item.dateFrom,
        dateTo = item.dateTo
    )
}

fun convertPredefinedFilterToCurrentFilter(item:PredefinedFilter):CurrentFilter{
    return CurrentFilter(
        market = item.market,
        product = item.product,
        ssessment = item.ssessment,
        language = item.language,
        dateTo = item.dateFrom,
        dateFrom = item.dateTo)

}

fun convertPredefinedFilterToNetworkNewsFilterObject(token:String,item:PredefinedFilter):NetworkNewsFilterObject{

    return NetworkNewsFilterObject(
        token=token,
        markets = convertStringWithCommasToRealArray(item.market),
        products = convertStringWithCommasToRealArray(item.product),
        ssessments = convertStringWithCommasToRealArray(item.ssessment),
        language = item.language,
        dateTo = item.dateFrom,
        dateFrom = item.dateTo)

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

