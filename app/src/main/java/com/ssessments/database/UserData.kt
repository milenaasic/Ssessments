package com.ssessments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "user_data_table")
data class UserData(

        @PrimaryKey
        var id:Int= 0,

        @ColumnInfo(name="username")
        var username:String="",

        @ColumnInfo(name="password")
        var password:String="",

        @ColumnInfo(name="token")
        var token:String=""

    )
