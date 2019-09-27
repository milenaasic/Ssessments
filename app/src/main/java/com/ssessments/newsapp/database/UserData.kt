package com.ssessments.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_data_table")
data class UserData(

        @PrimaryKey
        @ColumnInfo(name="username")
        var username:String,

        @ColumnInfo(name="password")
        var password:String,

        @ColumnInfo(name="token")
        var token:String

    )
