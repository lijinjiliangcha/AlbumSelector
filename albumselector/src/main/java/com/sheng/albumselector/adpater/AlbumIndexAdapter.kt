package com.sheng.albumselector.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sheng.albumselector.R
import com.sheng.albumselector.adpater.holder.IndexHolder

class AlbumIndexAdapter : RecyclerView.Adapter<IndexHolder> {

    private val context: Context
    private var dataList: ArrayList<String>? = null
    var onItemClickListener: ((String, Int) -> Unit)? = null

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_album_list, parent,false)
        return IndexHolder(view)
    }

    override fun onBindViewHolder(holder: IndexHolder, position: Int) {
        val data = dataList!![position]
        holder.tv.text = data
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data, position)
        }
    }

    fun setData(dataList: ArrayList<String>?) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

}