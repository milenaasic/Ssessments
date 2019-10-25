package com.ssessments.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssessments.newsapp.utilities.Language
import com.ssessments.newsapp.utilities.NO_DATE_SELECTED_VALUE

@Entity(tableName = "current_filter_table")
data class CurrentFilter (

        @PrimaryKey(autoGenerate = true)
        val ID:Long=1L,

        @ColumnInfo(name="market")
        val market:String="",

        @ColumnInfo(name = "product")
        val product:String="",

        @ColumnInfo(name = "ssessment")
        val ssessment:String="",

        @ColumnInfo(name="language")
        val language:String=Language.ENGLISH.value,

        @ColumnInfo(name = "filter_date_from")
        val dateFrom:String= NO_DATE_SELECTED_VALUE,

        @ColumnInfo(name = "filter_date_to")
        val dateTo:String= NO_DATE_SELECTED_VALUE

        )


