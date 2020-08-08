package com.ashok.omdbsearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ashok.omdbsearch.db.dao.SearchDao
import com.ashok.omdbsearch.model.SearchItem
import com.ashok.omdbsearch.util.DATABASE_NAME

@Database(entities = [SearchItem::class], version = 1)
public abstract class OMDbDatabase : RoomDatabase() {

    abstract fun searchItemDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: OMDbDatabase? = null

        fun getDatabase(context: Context): OMDbDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OMDbDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}