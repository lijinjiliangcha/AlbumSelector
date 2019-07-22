package com.sheng.albumselector

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.sheng.albumselector.entity.AlbumEntity
import java.io.File

class AlbumLoaderCallbacks(private val mContext: Context) : LoaderManager.LoaderCallbacks<Cursor> {

    private val mAlbumList by lazy { ArrayList<AlbumEntity>() }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return Utils.getPhotoLoader(mContext)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        if (cursor == null) {
            Log.e("测试", "查询出错，没有数据")
            return
        }
        val path = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val image = File(cursor.getString(path))
            if (image.exists()) {
                val parent = image.parentFile
                if (parent != null && parent.exists() && parent.isDirectory) {
                    var album: AlbumEntity? = mAlbumList.find { it.mName == parent.name }
                    if (album != null) {
                        album.mList?.add(image.absolutePath)
                        Log.i("图片路径", " = ${image.absolutePath}")
                    } else {
                        val album2 = AlbumEntity()
                        val list = ArrayList<String>()
                        list.add(image.absolutePath)
                        album2.mName = parent.name
                        album2.mList = list
                        album = album2
                        mAlbumList.add(album)
                    }
                }
            }
            cursor.moveToNext()
        }
        cursor.close()

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    fun getData(): ArrayList<AlbumEntity> {
        return mAlbumList
    }

}
