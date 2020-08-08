package com.ashok.omdbsearch.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashok.omdbsearch.R
import com.ashok.omdbsearch.adapter.SearchResultsAdapter
import com.ashok.omdbsearch.model.SearchItem
import com.ashok.omdbsearch.util.CommonUtils
import com.ashok.omdbsearch.util.DEFAULT_SEARCH_TERM
import com.ashok.omdbsearch.util.Status
import com.ashok.omdbsearch.viewmodel.OMDbViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var omDbViewModel: OMDbViewModel
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    private val defaultResults = mutableListOf<SearchItem>()

    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem

    private val searchText = StringBuilder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        searchResultsAdapter = SearchResultsAdapter()
        searchItemsRcv.adapter = searchResultsAdapter
        searchItemsRcv.layoutManager = LinearLayoutManager(this)

        omDbViewModel = ViewModelProvider(
            this,
            OMDbViewModel.OMDbViewModelFactory
        ).get(OMDbViewModel::class.java)


        omDbViewModel.defaultOmDbSearchResponseFromDB.observe(this, Observer {

            currentSearchUi(DEFAULT_SEARCH_TERM)

            if (it.isEmpty()) {
                // no data then call network
                omDbViewModel.defaultSearchMovieAndTvShows(DEFAULT_SEARCH_TERM)
            } else {
                defaultResults.clear()
                defaultResults.addAll(it)
                searchResultsAdapter.setDataSource(it)
                showRecycleView()
            }
            loading.visibility = View.GONE
        })

        omDbViewModel.defaultOmDbSearchResponse.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    it.data?.let { results ->
                        if (results.isEmpty()) {
                            updateEmptyView(
                                getString(R.string.no_data_found),
                                true
                            )
                        } else {
                            defaultResults.clear()
                            defaultResults.addAll(results)
                            searchResultsAdapter.setDataSource(results)
                            showRecycleView()
                        }
                        loading.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    loading.visibility = View.GONE

                    if (CommonUtils.isNetworkAvailable(this)) {
                        updateEmptyView(it.message ?: "Error occurred", true)
                    } else {
                        updateEmptyView(
                            getString(R.string.lbl_no_internet),
                            true
                        )
                    }

                }
            }
        })


        omDbViewModel.omdbResponse.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    it.data?.let { results ->
                        if (results.search!!.isEmpty()) {
                            updateEmptyView(
                                getString(R.string.no_data_found),
                                false
                            )
                        } else {
                            searchResultsAdapter.setDataSource(results.search!!)
                            showRecycleView()
                        }
                        loading.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    loading.visibility = View.GONE
                    //clear
                    searchResultsAdapter.setDataSource(emptyList())
                    if (CommonUtils.isNetworkAvailable(this)) {
                        updateEmptyView(it.message ?: "Error occurred")
                    }else{
                        updateEmptyView(getString(R.string.lbl_no_internet))
                    }
                }
            }
        })

        searchItemsRetryBtn.setOnClickListener {
            if (CommonUtils.isNetworkAvailable(this)) {
                omDbViewModel.defaultSearchMovieAndTvShows(DEFAULT_SEARCH_TERM)
            } else {
                updateEmptyView(
                    getString(R.string.lbl_no_internet),
                    true
                )
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_suggesions, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu.findItem(R.id.search)
        (searchMenuItem.actionView as SearchView).apply {
            searchView = this
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@MainActivity)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        //Collapse the action view
        CommonUtils.hideKeyboard(this, searchView)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {

        // strangely - this keeps getting called even if user is not changing text !
        // so we only react when there is an actual change

        currentSearchUi(newText)
        if (newText == searchText.toString()) {
            return true
        }

        if (newText.isBlank()) {
            currentSearchUi(DEFAULT_SEARCH_TERM)
            if (defaultResults.isEmpty()) {
                // no data then call network
                omDbViewModel.defaultSearchMovieAndTvShows(DEFAULT_SEARCH_TERM)
            } else {
                showRecycleView()
                searchResultsAdapter.setDataSource(defaultResults)
            }
            return true
        }
        // remember the change
        searchText.setLength(0)
        searchText.append(newText)

        omDbViewModel.searchMovieAndTvShows(newText)

        return true // true means text change is handled here
    }


    private fun showRecycleView() {
        searchItemsRcv.visibility = View.VISIBLE
        searchItemsEmptyInfoCv.visibility = View.GONE
    }

    private fun updateEmptyView(msg: String = "No data found", canShowRetry: Boolean = false) {
        searchItemsRcv.visibility = View.GONE
        searchItemsEmptyInfoCv.visibility = View.VISIBLE
        searchItemsEmptyTv.visibility = View.VISIBLE
        searchItemsEmptyTv.text = msg
        searchItemsRetryBtn.visibility = if (canShowRetry) View.VISIBLE else View.GONE
    }

    private fun currentSearchUi(searchTerm: String) {
        searchTermTv.text = getString(R.string.format_search_text, searchTerm)
    }


}