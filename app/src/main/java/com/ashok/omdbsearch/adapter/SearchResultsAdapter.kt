package com.ashok.omdbsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashok.omdbsearch.R
import com.ashok.omdbsearch.model.SearchItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.li_search_item.view.*

class SearchResultsAdapter () : RecyclerView.Adapter<SearchResultsAdapter.SearchItemViewHolder>() {

    private var searchItems = mutableListOf<SearchItem>() // Cached copy of words

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchItemViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.li_search_item, viewGroup, false)
        return SearchItemViewHolder(view)
    }



    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val item = searchItems[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int = searchItems.size


    internal fun setDataSource(searchItemsResult: List<SearchItem>) {
        this.searchItems.clear()
        this.searchItems.addAll(searchItemsResult)
        notifyDataSetChanged()
    }

    inner class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var item: SearchItem
        private val searchItemName: TextView = itemView.itemName
        private val searchItemYear: TextView = itemView.itemYear
        private val searchItemIv: ImageView = itemView.itemIv

        fun bind(item: SearchItem) {
            this.item = item;
            searchItemName.text = item.title
            searchItemYear.text = item.year

            Glide.with(searchItemIv.context)
                .load(item.poster)
                .placeholder(R.drawable.ic_stub_image)
                .into(searchItemIv)
        }
    }
}

