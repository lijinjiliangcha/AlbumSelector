package com.sheng.albumselector.adpater

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.sheng.albumselector.R

/**
 * 相册集合适配器
 */
class AlbumCursorAdapter : CursorAdapter {

    constructor(context: Context, c: Cursor?, autoRequery: Boolean) : super(context, c, autoRequery)

    override fun newView(context: Context?, c: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.item_album_list, parent, false)
    }

    override fun bindView(view: View, context: Context?, c: Cursor?) {
        Log.i("测试", "cursor = $c")
//        view.findViewById<TextView>(R.id.tv_content).setText("TODO-1")
    }

}