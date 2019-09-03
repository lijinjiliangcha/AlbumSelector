package com.sheng.albumselector.adpater.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sheng.albumselector.R

class AlbumHolder : RecyclerView.ViewHolder {

    val iv_img: ImageView
    val tv_select: TextView
    var path = ""
    var isSelect: Boolean = false
        set(value) {
            if (value)
                tv_select.setBackgroundResource(R.drawable.shape_checked)
            else {
                tv_select.setBackgroundResource(R.drawable.shape_unchecked)
                tv_select.text = ""
            }
            field = value
        }

    constructor(itemView: View) : super(itemView) {
        iv_img = itemView.findViewById(R.id.iv_img)
        tv_select = itemView.findViewById(R.id.tv_select)
    }

//    fun isSelect(flag: Boolean) {
//        isSelect = flag
//        if (flag)
//            iv_select.setImageResource(R.mipmap.solid)
//        else
//            iv_select.setImageResource(R.mipmap.hollow)
//    }
}