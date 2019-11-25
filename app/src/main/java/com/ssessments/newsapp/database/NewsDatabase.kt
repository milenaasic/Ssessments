package com.ssessments.newsapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ssessments.newsapp.utilities.*


private const val NAME="News Database"

@Database(entities = [NewsItem::class,FilterItem::class,UserData::class,CurrentFilter::class,PredefinedFilter::class],version = 3)
abstract class NewsDatabase:RoomDatabase(){

    abstract val newsDatabaseDao:NewsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            synchronized(this) {

                var instance = INSTANCE
                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        NAME
                    ).fallbackToDestructiveMigration()
                        .addCallback (object:Callback(){
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                val myvalues=ContentValues().apply {
                                    put("market",Markets.ALL_MARKETS.value)
                                    put("product",Products.ALL_PRODUCTS.value)
                                    put("ssessment",Ssessments.ALL_SERVICES.value)
                                    put("language",Language.ENGLISH.value)
                                    put("filter_date_from", NO_DATE_SELECTED_VALUE)
                                    put("filter_date_to", NO_DATE_SELECTED_VALUE)
                                }

                                val myvalues2=ContentValues().apply {
                                    put("username", EMPTY_USERNAME)
                                    put("password", EMPTY_PASSWORD)
                                    put("token", EMPTY_TOKEN)
                                    put("firebase_id", EMPTY_FIREBASEID)

                                 }
                                val myvalues3=ContentValues().apply {
                                    put("news_ID", -1)
                                    put("news_title", NO_RESULT)
                                    put("tags", NO_RESULT)
                                    put("date_time", NO_RESULT)
                                    put("access_type", NO_RESULT)
                                }

                                try{
                                    db.insert("current_filter_table",CONFLICT_IGNORE,myvalues)
                                    db.insert("user_data_table", CONFLICT_IGNORE,myvalues2)
                                    db.insert("last_news_list_table", CONFLICT_IGNORE,myvalues3)

                                }catch (e:Exception){
                                    Log.w("my database error",e)}

                            }
                        })
                        .build()
                    INSTANCE = instance
                }

                return instance

            }
        }
    }
}