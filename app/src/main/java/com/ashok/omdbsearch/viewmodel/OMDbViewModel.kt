package com.ashok.omdbsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ashok.omdbsearch.model.SearchItem
import com.ashok.omdbsearch.model.SearchResponse
import com.ashok.omdbsearch.repository.OMDbRepository
import com.ashok.omdbsearch.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OMDbViewModel(private var repository: OMDbRepository) : ViewModel() {


    //db search
    val defaultOmDbSearchResponseFromDB: LiveData<List<SearchItem>> = repository.defaultOmDbSearchFromDb

    val defaultOmDbSearchResponse: LiveData<Resource<List<SearchItem>>> = repository.defaultOmDbSearchResponse
    fun defaultSearchMovieAndTvShows(searchTerm: String) {
        viewModelScope.launch{
            repository.defaultSearchMovieAndTvShows(searchTerm)
        }
    }


    //realtime search
    val omdbResponse: LiveData<Resource<SearchResponse>> = repository.omDbSearchResponse

    private var searchJob: Job? = null
    private var debouncePeriod: Long = 500
    fun searchMovieAndTvShows(searchTerm: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            repository.searchMovieAndTvShows(searchTerm)
        }
    }

    object OMDbViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OMDbViewModel::class.java)) {
                return OMDbViewModel(
                    repository = OMDbRepository()
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}


