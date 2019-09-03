package com.sheng.albumselector.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Created by Administrator on 2017/6/15.
 */
object ResourcesUtils {

    fun getColor(context: Context, id: Int): Int {
        return context.resources.getColor(id)
    }

    fun getString(context: Context, id: Int): String {
        return context.resources.getString(id)
    }

    fun getDimens(context: Context, id: Int): Int {
        return context.resources.getDimensionPixelSize(id)
    }

    fun getMipmap(context: Context, id: Int): Bitmap {
        return BitmapFactory.decodeResource(context.getResources(), id)
    }

}