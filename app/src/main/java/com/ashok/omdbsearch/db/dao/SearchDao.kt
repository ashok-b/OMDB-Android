package com.ashok.omdbsearch.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashok.omdbsearch.model.SearchItem

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<SearchItem>)

    @Query("SELECT * FROM tbl_search_movie_tv_shows WHERE search_name = :searchTerm")
    fun getSearchItemsLiveData(searchTerm : String): LiveData<List<SearchItem>>

    @Query("SELECT * FROM tbl_search_movie_tv_shows WHERE search_name Like :searchTerm")
    suspend fun getSearchItems(searchTerm : String): List<SearchItem>

    @Query("DELETE FROM tbl_search_movie_tv_shows")
    suspend fun deleteAll()


}