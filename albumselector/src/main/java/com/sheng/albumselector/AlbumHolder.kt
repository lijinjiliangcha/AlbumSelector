package com.sheng.albumselector

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class AlbumHolder : RecyclerView.ViewHolder {

    val iv_img: ImageView
    val iv_select: ImageView
    var isSelect: Boolean = false
        set(value) {
            if (value)
                iv_select.setImageResource(R.mipmap.solid)
            else
                iv_select.setImageResource(R.mipmap.hollow)
            field = value
        }

    constructor(itemView: View) : super(itemView) {
        iv_img = itemView.findViewById(R.id.iv_img)
        iv_select = itemView.findViewById(R.id.iv_select)
    }

//    fun isSelect(flag: Boolean) {
//        isSelect = flag
//        if (flag)
//            iv_select.setImageResource(R.mipmap.solid)
//        else
//            iv_select.setImageResource(R.mipmap.hollow)
//    }
}