package com.sheng.albumselector.widget

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sheng.albumselector.R
import com.sheng.albumselector.adpater.AlbumIndexAdapter
import com.sheng.albumselector.utils.ResourcesUtils

class AlbumIndexPopupWindow : PopupWindow {

    private val context: Context
    //显示时作为挂载的view
    var anchor: View? = null
    private val rv: RecyclerView
    private val mAdapter: AlbumIndexAdapter
    //动画
    private var enterAnimation: Animation? = null
    private var exitAnimation: Animation? = null
    private var isShowAnim = false

    constructor(context: Context) : super(View.inflate(context, R.layout.popup_index, null),
        GridLayout.LayoutParams.MATCH_PARENT,
        ResourcesUtils.getDimens(context, R.dimen.albumIndexMaxHeight)) {
        this.context = context
        isOutsideTouchable = true
//        setBackgroundDrawable(BitmapDrawable())
        isFocusable = true//获得焦点，防止事件穿透
        super.setAnimationStyle(0)//去掉默认动画

        rv = contentView.findViewById(R.id.rv)
        mAdapter = AlbumIndexAdapter(context)
        rv.adapter = mAdapter

        animationStyle = R.style.AlbumIndexPopupWindow_animStyle
    }

    override fun setAnimationStyle(animationStyle: Int) {
        //从resource中获取style
        val typeArray = context.resources.obtainTypedArray(animationStyle)
        val enterId = typeArray.getResourceId(R.styleable.AlbumIndexPopupWindow_EnterAnimation, 0)
        val exitId = typeArray.getResourceId(R.styleable.AlbumIndexPopupWindow_ExitAnimation, 0)
        typeArray.recycle()
        Log.i("测试", "enterId = $enterId，exitId = $exitId")

        enterAnimation = if (enterId != 0) AnimationUtils.loadAnimation(context, enterId) else null
        exitAnimation = if (exitId != 0) AnimationUtils.loadAnimation(context, exitId) else null

        enterAnimation?.setAnimationListener(enterListener)
        exitAnimation?.setAnimationListener(exitListener)
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
            if (isShowAnim)
                return
            showAsDropDown(anchor)
            enterAnimation.let { contentView.startAnimation(enterAnimation) }
        }
    }

    override fun dismiss() {
        if (isShowAnim)
            return
        if (exitAnimation != null)
            contentView.startAnimation(exitAnimation)
        else
            super.dismiss()
    }

    fun setClickItemChangeListener(listener: (String, Int) -> Unit) {
        mAdapter.onItemClickListener = listener
    }

    private val enterListener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            isShowAnim = false
        }

        override fun onAnimationStart(p0: Animation?) {
            isShowAnim = true
        }
    }

    private val exitListener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            super@AlbumIndexPopupWindow.dismiss()
            isShowAnim = false
        }

        override fun onAnimationStart(p0: Animation?) {
            isShowAnim = true
        }

    }
}