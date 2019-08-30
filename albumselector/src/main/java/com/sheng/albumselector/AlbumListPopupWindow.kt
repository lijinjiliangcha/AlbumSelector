package com.sheng.albumselector

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import androidx.appcompat.widget.ListPopupWindow

class AlbumListPopupWindow : ListPopupWindow {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private var mAdapter: ArrayAdapter<String>? = null
    private var mContext: Context? = null
    private val mMaxCount = 6// 弹窗的展现上限个数

    private var mCursorAdapter: CursorAdapter? = null

    // 监听器
    private var mOnClickItemChangeListener: ((String) -> Unit)? = null

    private fun init(context: Context) {
        this.mContext = context
        isModal = true
        val density = context.resources.displayMetrics.density
        setContentWidth((216 * density).toInt())
        horizontalOffset = (16 * density).toInt()
        verticalOffset = (-0 * density).toInt()
        setOnItemClickListener { parent, view, position, id ->
            mOnClickItemChangeListener?.invoke(mAdapter?.getItem(position) ?: "")
            dismiss()
        }
    }

    fun setAdapter(list: ArrayList<String>) {
        mCursorAdapter = AlbumCursorAdapter(mContext!!, null, false)
        mAdapter = ArrayAdapter(mContext!!, R.layout.item_album_list, list)
        setAdapter(mAdapter)
        val itemHeight = mContext!!.resources.getDimensionPixelSize(R.dimen.albumItemHeight)
        // 限制弹窗的高度
        height = if (mAdapter!!.count >= mMaxCount) {
            mMaxCount * itemHeight
        } else {
            mAdapter!!.count * itemHeight
        }
    }

    fun setClickItemChangeListener(listener: (String) -> Unit) {
        this.mOnClickItemChangeListener = listener
    }


}