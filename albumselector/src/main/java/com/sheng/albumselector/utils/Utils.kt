package com.sheng.albumselector.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.loader.content.CursorLoader
import org.jetbrains.annotations.Contract
import java.util.ArrayList
import android.app.Activity
import android.text.TextUtils
import android.os.Build
import android.provider.Settings
import android.view.View


object Utils {

    // 获取系统保存的数据库
    fun getImagesMedia(context: Context): ArrayList<String> {
        val list = ArrayList<String>()
        //获取系统数据库保存的图库
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " desc"
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, sortOrder)
        if (cursor != null) {
            val iId = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val iPath = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                var id = cursor.getString(iId);
                var path = cursor.getString(iPath);
//                Log.i("测试", "path = $path")
                list.add(path);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list
    }

    fun getImagesMedia2(context: Context): ArrayList<String> {
        val list = ArrayList<String>()
        //获取系统数据库保存的图库
        val loader = CursorLoader(
            context,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA),
            null,
            null,
            null
        )
        val cursor: Cursor? = loader.loadInBackground()
        if (cursor != null) {
            val iId = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val iPath = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
//                var id = cursor.getString(iId);
                val path = cursor.getString(iPath);
                Log.i("测试", "path = $path")
                list.add(path);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return list
    }

    fun getPhotoLoader(context: Context): CursorLoader {
        ContactsContract.Contacts.CONTENT_URI
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " desc"
        val projection = arrayOf<String>(MediaStore.Images.Media._ID
            , MediaStore.Images.Media.DATA)
        return CursorLoader(context, uri, projection, null, null, sortOrder)
    }

    /**
     * 获取虚拟按键的高度
     * 1. 全面屏下
     * 1.1 开启全面屏开关-返回0
     * 1.2 关闭全面屏开关-执行非全面屏下处理方式
     * 2. 非全面屏下
     * 2.1 没有虚拟键-返回0
     * 2.1 虚拟键隐藏-返回0
     * 2.2 虚拟键存在且未隐藏-返回虚拟键实际高度
     */
    fun getNavigationBarHeightIfRoom(context: Context): Int {
        return if (navigationGestureEnabled(context)) {
            0
        } else getCurrentNavigationBarHeight(context as Activity)
    }

    /**
     * 全面屏（是否开启全面屏开关 0 关闭  1 开启）
     *
     * @param context
     * @return
     */
    fun navigationGestureEnabled(context: Context): Boolean {
        val state = Settings.Global.getInt(context.contentResolver, getDeviceInfo(), 0)
        return state != 0
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo都可以）
     *
     * @return
     */
    fun getDeviceInfo(): String {
        val brand = Build.BRAND
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"

        return if (brand.equals("HUAWEI", ignoreCase = true)) {
            "navigationbar_is_min"
        } else if (brand.equals("XIAOMI", ignoreCase = true)) {
            "force_fsg_nav_bar"
        } else if (brand.equals("VIVO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else if (brand.equals("OPPO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else {
            "navigationbar_is_min"
        }
    }

    /**
     * 非全面屏下 虚拟键实际高度(隐藏后高度为0)
     * @param activity
     * @return
     */
    fun getCurrentNavigationBarHeight(activity: Activity): Int {
        return if (isNavigationBarShown(activity)) {
            getNavigationBarHeight(activity)
        } else {
            0
        }
    }

    /**
     * 非全面屏下 虚拟按键是否打开
     * @param activity
     * @return
     */
    fun isNavigationBarShown(activity: Activity): Boolean {
        //虚拟键的view,为空或者不可见时是隐藏状态
        val view = activity.findViewById<View>(android.R.id.navigationBarBackground) ?: return false
        val visible = view.getVisibility()
        return if (visible == View.GONE || visible == View.INVISIBLE) {
            false
        } else {
            true
        }
    }

    /**
     * 非全面屏下 虚拟键高度(无论是否隐藏)
     * @param context
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}