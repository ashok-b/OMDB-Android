package com.ashok.omdbsearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ashok.academy.omdb.api.RetrofitClient
import com.ashok.omdbsearch.BuildConfig
import com.ashok.omdbsearch.OMDBApp
import com.ashok.omdbsearch.api.OMDbApi
import com.ashok.omdbsearch.db.dao.SearchDao
import com.ashok.omdbsearch.model.SEARCH_RESPONSE_FALSE
import com.ashok.omdbsearch.model.SEARCH_RESPONSE_TRUE
import com.ashok.omdbsearch.model.SearchItem
import com.ashok.omdbsearch.model.SearchResponse
import com.ashok.omdbsearch.util.DEFAULT_SEARCH_TERM
import com.ashok.omdbsearch.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OMDbRepository() {

   // private val omdbDao : SearchDao = OMDBApp.imdbDatabase().searchItemDao()

    private var omdbDao : SearchDao
    init {
        val db = OMDBApp.imdbDatabase()
        omdbDao = db.searchItemDao()
    }

    private var  omDbApi : OMDbApi = RetrofitClient.createService(OMDbApi::class.java)

    val defaultOmDbSearchFromDb: LiveData<List<SearchItem>> = omdbDao.getSearchItemsLiveData(DEFAULT_SEARCH_TERM)

    //
    private var _defaultOmDbSearchResponse = MutableLiveData<Resource<List<SearchItem>>>()

    val defaultOmDbSearchResponse: LiveData<Resource<List<SearchItem>>>
        get() = _defaultOmDbSearchResponse

    suspend fun defaultSearchMovieAndTvShows(searchTerm : String) {

        try {
            //background
            val result = withContext(Dispatchers.IO) {
                _defaultOmDbSearchResponse.postValue(Resource.loading(data = null))
                val response = omDbApi.searchMovieAndTvShows(searchTerm, BuildConfig.OMDB_API_KEY)
                if (response.isSuccessful) {

                    if(response.body()?.response.equals(SEARCH_RESPONSE_TRUE, ignoreCase = true)){
                        val results = response.body()?.search.orEmpty()
                        results.map { it.searchTerm = searchTerm }
                        omdbDao.deleteAll()
                        omdbDao.insertAll(results)
                    }else if(response.body()?.response.equals(SEARCH_RESPONSE_FALSE, ignoreCase = true)){
                        val erroMsg = response.body()?.errorMsg?: "Something went wrong"
                        _defaultOmDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
                    }else{
                        val erroMsg = response.errorBody()?.string()?: "Unexpected went wrong"
                        _defaultOmDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
                    }
                } else {
                    val erroMsg = response.errorBody()?.string()?: "Something went wrong"
                    _defaultOmDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
                }
            }
        } catch (ex: Throwable) {
            val erroMsg = ex.localizedMessage?: "Something went wrong"
            _defaultOmDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
        }
    }
    // ------ end ------

    //


    // ----- start ----
    private var _omDbSearchResponse = MutableLiveData<Resource<SearchResponse>>()
    val omDbSearchResponse: LiveData<Resource<SearchResponse>>
        get() = _omDbSearchResponse

    suspend fun searchMovieAndTvShows(searchTerm : String) {

        try {
            //background
            val result = withContext(Dispatchers.IO) {
                _defaultOmDbSearchResponse.postValue(Resource.loading(data = null))
                val response = omDbApi.searchMovieAndTvShows(searchTerm,BuildConfig.OMDB_API_KEY)
                if (response.isSuccessful) {
                    if(response.body()?.response.equals(SEARCH_RESPONSE_TRUE, ignoreCase = true)){
                        val results = response.body()?.search.orEmpty()
                        results.map { it.searchTerm = searchTerm }
                        _omDbSearchResponse.postValue(Resource.success(data = response.body()!!))
                    }else if(response.body()?.response.equals(SEARCH_RESPONSE_FALSE, ignoreCase = true)){
                        val erroMsg = response.body()?.errorMsg?: "Something went wrong"
                        _omDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
                    }else{
                        val erroMsg = response.errorBody()?.string()?: "Unexpected went wrong"
                        _omDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
                    }
                } else {
                    val erroMsg = response.errorBody()?.string()?: "Something went wrong"
                    _omDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
                }
            }
        } catch (ex: Throwable) {
            val erroMsg = ex.localizedMessage?: "Something went wrong"
            _omDbSearchResponse.postValue(Resource.error(data = null, message = erroMsg))
        }
    }

    // ------ end ------
}