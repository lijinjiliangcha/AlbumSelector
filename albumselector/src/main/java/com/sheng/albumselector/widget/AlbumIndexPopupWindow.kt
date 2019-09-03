package com.sheng.albumselector.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sheng.albumselector.R
import com.sheng.albumselector.adpater.AlbumIndexAdapter
import com.sheng.albumselector.utils.ResourcesUtils

class AlbumIndexPopupWindow : PopupWindow {

    //显示时作为挂载的view
    var anchor: View? = null
    private val rv: RecyclerView
    private val mAdapter: AlbumIndexAdapter

    constructor(context: Context) : super(View.inflate(context, R.layout.popup_index, null),
        GridLayout.LayoutParams.MATCH_PARENT,
        ResourcesUtils.getDimens(context, R.dimen.albumIndexMaxHeight)) {

        isOutsideTouchable = true
//        setBackgroundDrawable(BitmapDrawable())
        isFocusable = true//获得焦点，防止事件穿透
        setAnimationStyle(0)//去掉默认动画

        rv = contentView.findViewById(R.id.rv)
        mAdapter = AlbumIndexAdapter(context)
        rv.adapter = mAdapter


    }

//    private fun init(context: Context) {
//        this.mContext = context
//        isModal = true
//        val density = context.resources.displayMetrics.density
//        setContentWidth((216 * density).toInt())
//        horizontalOffset = (16 * density).toInt()
//        verticalOffset = (-0 * density).toInt()
//        setOnItemClickListener { parent, view, position, id ->
//            mOnClickItemChangeListener?.invoke(mAdapter?.getItem(position) ?: "")
//            dismiss()
//    }
//    }

    fun setData(list: ArrayList<String>) {
        mAdapter.setData(list)
    }

    fun show() {
        anchor?.let {
            showAsDropDown(anchor)
        }
    }

    fun setClickItemChangeListener(listener: (String, Int) -> Unit) {
        mAdapter.onItemClickListener = listener
    }


}