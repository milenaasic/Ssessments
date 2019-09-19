package com.ssessments.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.ssessments.utilities.*


private const val NAME="News Database"

@Database(entities = [NewsItem::class,FilterItem::class,UserData::class,CurrentFilter::class,PredefinedFilter::class],version = 2)
abstract class NewsDatabase:RoomDatabase(){

    abstract val newsDatabaseDao:NewsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            synchronized(this) {
                Log.d("my DBOPENED","Database creating.")
                var instance = INSTANCE
                if (instance == null) {
                    Log.d("my DBOPENED","Database creating instance jenull.")
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        NAME
                    ).fallbackToDestructiveMigration()
                        .addCallback (object:Callback(){
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d("my DBOPENED","Database has been opened.")
                                val myvalues=ContentValues().apply {
                                    put("market",Markets.ALL_MARKETS.value)
                                    put("product",Products.ALL_PRODUCTS.value)
                                    put("ssessment",Ssessments.ALL_SERVICES.value)
                                    put("language",Language.ENGLISH.value)
                                    put("filter_date_from", DATE_SELECT_TEXT)
                                    put("filter_date_to", DATE_SELECT_TEXT)
                                }
                                try{db. insert("current_filter_table",CONFLICT_IGNORE,myvalues)
                                    Log.d("my DBOPENED","try block.")
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