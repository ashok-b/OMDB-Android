package com.ashok.omdbsearch.api

import com.ashok.omdbsearch.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApi {
    //http://www.omdbapi.com/?s=friends&apikey=yourkey
    @GET("/")
    suspend fun searchMovieAndTvShows(
        @Query("s") searchQueryParam: String,
        @Query("apikey") apiKey: String
    ): Response<SearchResponse>

}