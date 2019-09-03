package com.sheng.albumselector.adpater.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sheng.albumselector.R

class IndexHolder : RecyclerView.ViewHolder {

    val tv: TextView

    constructor(itemView: View) : super(itemView) {
        tv = itemView.findViewById(R.id.tv)
    }
}