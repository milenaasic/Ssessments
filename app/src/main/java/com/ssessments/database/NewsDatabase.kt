package com.ssessments.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


private const val NAME="News Database"

@Database(entities = [NewsItem::class,FilterItem::class],version = 1)
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
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance

            }
        }
    }
}