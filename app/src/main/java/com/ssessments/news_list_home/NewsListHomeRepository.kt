package com.ssessments.news_list_home

import androidx.lifecycle.LiveData
import com.ssessments.database.NewsDatabase
import com.ssessments.database.NewsItem

class NewsListHomeRepository(val database: NewsDatabase) {

    val datasource=database.newsDatabaseDao

    fun insert(item: NewsItem){
       datasource.insert(item)
    }

    fun getNewsList():List<NewsItem>{
        return datasource.getAllNews()
    }

    //ovo posle uradi na nivou baze
    fun insertNewsList(list:List<NewsItem>){
        for(item in list) datasource.insert(item)
    }
}
