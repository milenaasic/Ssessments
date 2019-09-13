package com.ssessments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.time.LocalDate
import java.util.*

@Entity(tableName = "saved_filters_table")
data class FilterItem(

    @PrimaryKey(autoGenerate = true)
    val ID:Long=0L,

    @ColumnInfo(name="filter_name")
    val filterName:String="My Filter",

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
