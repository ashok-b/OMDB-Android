package com.ashok.omdbsearch

import android.app.Application
import com.ashok.omdbsearch.db.OMDbDatabase

class OMDBApp : Application() {

    companion object{
        private lateinit var omDbDatabase: OMDbDatabase

        fun imdbDatabase(): OMDbDatabase {
            return omDbDatabase
        }
    }

    override fun onCreate() {
        super.onCreate()
        omDbDatabase = OMDbDatabase.getDatabase(this);
    }
}