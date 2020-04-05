package com.elenamakeeva.alfanewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elenamakeeva.alfanewsapp.R
import com.elenamakeeva.alfanewsapp.api.AbstractNews
import com.elenamakeeva.alfanewsapp.api.Item

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ItemViewHolder> () {

    interface OnNewsClickListener {
        fun onNewsClick(position: Int)
    }

    var onNewsClickListener : OnNewsClickListener? = null
    var newsList: List<AbstractNews>? = null

    init {
        newsList = arrayListOf()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_news_list, viewGroup, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = newsList?.size ?: 0

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val news = newsList?.get(position)
        holder.title?.text = news?.title
        holder.pubDate?.text = news?.pubDate
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView? = null
        internal var pubDate: TextView? = null

        init {
            title = itemView.findViewById(R.id.news_title)
            pubDate = itemView.findViewById(R.id.news_pubDate)
            itemView.setOnClickListener {
                onNewsClickListener?.onNewsClick(adapterPosition)
            }
        }
    }
}