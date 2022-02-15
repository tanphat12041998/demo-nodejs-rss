package com.news.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_rss.view.*

class RssItemAdapter : RecyclerView.Adapter<RssItemAdapter.ViewHolder>() {

    private var rssList: MutableList<RssItem> = mutableListOf()
    private var onItemClick: RssItemClick? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rss, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            rssList[position].also { item ->
                Glide.with(this)
                    .load(item.image)
                    .into(image)
                title.text = item.title
                description.text = item.description
                pubDate.text = item.pubDate
                setOnClickListener {
                    onItemClick?.onClick(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = rssList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<RssItem>) {
        this.rssList.clear()
        this.rssList.addAll(items)
        this.notifyDataSetChanged()
    }

    fun setOnItemClick(onClick: RssItemClick) {
        this.onItemClick = onClick
    }
}

interface RssItemClick {
    fun onClick(item: RssItem)
}