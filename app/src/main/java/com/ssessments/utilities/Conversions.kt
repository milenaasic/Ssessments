package com.ssessments.utilities

import com.ssessments.database.NewsItem
import com.ssessments.network.NetworkNewsItem

fun convertNetworkToDatabaseNewsItem(networkNewsList:List<NetworkNewsItem>):List<NewsItem>{
    var resultList= ArrayList<NewsItem>()

    for(item in networkNewsList){
        resultList.add(NewsItem(0L,item.newsID,item.title,item.tags,item.date_time,item.access))

    }
    return resultList
}