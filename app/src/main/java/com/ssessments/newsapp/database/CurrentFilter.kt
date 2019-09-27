package com.ssessments.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_filter_table")
data class CurrentFilter (

        @PrimaryKey(autoGenerate = true)
        val ID:Long=0L,

        @ColumnInfo(name="market")
        val market:String="",

        @ColumnInfo(name = "product")
        val product:String="",

        @ColumnInfo(name = "ssessment")
        val ssessment:String="",

        @ColumnInfo(name="language")
        val language:String="",

        @ColumnInfo(name = "filter_date_from")
        val dateFrom:String="",

        @ColumnInfo(name = "filter_date_to")
        val dateTo:String=""

        )


