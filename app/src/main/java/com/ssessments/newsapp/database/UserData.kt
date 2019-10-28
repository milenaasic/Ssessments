package com.ssessments.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssessments.newsapp.utilities.EMPTY_FIREBASEID
import com.ssessments.newsapp.utilities.EMPTY_PASSWORD
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import com.ssessments.newsapp.utilities.EMPTY_USERNAME


@Entity(tableName = "user_data_table")
data class UserData(

        @PrimaryKey(autoGenerate = true)
        val ID:Long=1L,

        @ColumnInfo(name="username")
        var username:String= EMPTY_USERNAME,

        @ColumnInfo(name="password")
        var password:String= EMPTY_PASSWORD,

        @ColumnInfo(name="token")
        var token:String= EMPTY_TOKEN,

        @ColumnInfo(name="firebase_id")
        var firebaseID:String= EMPTY_FIREBASEID

    )
