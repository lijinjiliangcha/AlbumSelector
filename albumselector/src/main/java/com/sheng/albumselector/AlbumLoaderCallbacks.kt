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

    val ALL = "全部"//全部数据分类

    // 存储相册对象，一个对象包括相册名、以及对应的相册内图片的路径列表
    private val mAlbumList by lazy { HashMap<String, ArrayList<String>>() }

    // 加载完成的回调
    private var mLoadFinishedListener: ((map: HashMap<String, ArrayList<String>>) -> Unit)? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return Utils.getPhotoLoader(mContext)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        if (cursor == null) {
            Log.e("测试", "查询出错，没有数据")
            mLoadFinishedListener?.invoke(HashMap())
            return
        }
        val path = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val allList = ArrayList<String>()
        while (!cursor.isAfterLast) {
            val image = File(cursor.getString(path))
            val parent = image.parentFile
            if (parent != null && parent.exists() && parent.isDirectory) {
                val parentName = parent.name
                var list = mAlbumList[parentName]
                if (list == null) {
                    list = ArrayList()
                    mAlbumList.put(parentName, list)
                }
                list.add(image.absolutePath)
                allList.add(image.absolutePath)
//                var album: AlbumEntity? = mAlbumList.find { it.mName == parent.name }
//                // 判断是否有对应 mName 为 parent.name 的相册实体类
//                if (album != null) {
//                    album.mList?.add(image.absolutePath)
//                    Log.i("图片路径", " = ${image.absolutePath}")
//                } else {
//                    // 无对应的相册实体类，则新建并赋值
//                    val album2 = AlbumEntity()
//                    val list = ArrayList<String>()
//                    list.add(image.absolutePath)
//                    album2.mName = parent.name
//                    album2.mList = list
//                    album = album2
//                    mAlbumList.add(album)
//                }
            }
            cursor.moveToNext()
        }
        mAlbumList.put(ALL, allList)
        mLoadFinishedListener?.invoke(mAlbumList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    // 获取相册实体类的数据
    fun getData(): HashMap<String, ArrayList<String>> {
        return mAlbumList
    }

    // 设置加载完成的回调
    fun setLoadFinishedListener(listener: (map: HashMap<String, ArrayList<String>>) -> Unit) {
        this.mLoadFinishedListener = listener
    }

}
