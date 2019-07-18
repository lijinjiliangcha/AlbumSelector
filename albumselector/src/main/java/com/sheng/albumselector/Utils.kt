package com.sheng.albumselector

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.util.ArrayList

object Utils {

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
                Log.i("测试", "path = $path")
                list.add(path);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list
    }
}