package com.ashok.omdbsearch.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(@SerializedName("Response")
                          val response: String = "",
                          @SerializedName("totalResults")
                          val totalResults: String = "",
                          @SerializedName("Error")
                          val errorMsg: String?,
                          @SerializedName("Search")
                          var search: List<SearchItem>?)



const val SEARCH_RESPONSE_TRUE = "TRUE"
const val SEARCH_RESPONSE_FALSE = "FASLE"