package com.ashok.omdbsearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_search_movie_tv_shows")
data class SearchItem(@SerializedName("Type")
                      val type: String = "",
                      @SerializedName("Year")
                      val year: String = "",
                      @PrimaryKey
                      @SerializedName("imdbID")
                      val imdbID: String = "",
                      @SerializedName("Poster")
                      val poster: String = "",
                      @SerializedName("Title")
                      val title: String = "",
                      @Expose(serialize = false , deserialize = false)
                      @ColumnInfo(name = "search_name")
                      var searchTerm: String = "")
