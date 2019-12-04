package com.ssessments.newsapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssessments.newsapp.utilities.*


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
        var firebaseID:String= EMPTY_FIREBASEID,

        @ColumnInfo(name="first_name")
        var firstName:String= EMPTY_FIRST_NAME,

        @ColumnInfo(name="last_name")
        var lastName:String= EMPTY_LAST_NAME,

        @ColumnInfo(name="access_type")
        var accessType:String= EMPTY_ACCESS_TYPE,

        @ColumnInfo(name="email")
        var email:String= EMPTY_EMAIL,

        @ColumnInfo(name="mobile_phone")
        var mobilephone:String= EMPTY_PHONE,

        @ColumnInfo(name="company")
        var company:String= EMPTY_COMPANY,

        @ColumnInfo(name="country")
        var country:String= EMPTY_COUNTRY

    )
