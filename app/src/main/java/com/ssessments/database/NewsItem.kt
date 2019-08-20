package com.ssessments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "last_news_list_table")
data class NewsItem(
    @PrimaryKey(autoGenerate = true)
    var id:Long= 0L,

    @ColumnInfo(name="news_ID")
    var newsID:Int=0,

    @ColumnInfo(name="news_title")
    var title:String="title",

    @ColumnInfo(name="tags")
    var tags:String="tags",

    @ColumnInfo(name="date_time")
    var date_time:String="date",

    @ColumnInfo(name = "access_type")
    var access:String="access"

)