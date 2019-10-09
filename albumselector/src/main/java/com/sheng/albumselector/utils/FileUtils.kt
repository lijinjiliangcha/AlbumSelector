package com.sheng.albumselector.utils

import android.content.Context
import android.os.Environment
import androidx.core.os.EnvironmentCompat
import java.io.File

object FileUtils {

    val KB = 1024
    val MB = 1024 * KB
    val GB = 1024 * MB

    //文件大小格式化
    fun getFileSizeStr(size: Long): String {
        val temp: Double
        if (size > GB) {
            temp = 1.0 * size / GB
            return String.format("%.2f", temp) + "G"
        } else if (size > MB) {
            temp = 1.0 * size / MB
            return String.format("%.2f", temp) + "M"
        } else if (size > KB) {
            temp = 1.0 * size / KB
            return String.format("%.2f", temp) + "Kb"
        } else {
            return "${size}b"
        }
    }

    //获取这个文件夹的大小
    fun getFolderSize(file: File): Long {
        if (file.isFile)
            return file.length()
        var size: Long = 0
        val files = file.listFiles()
        files?.forEach {
            size += getFolderSize(it)
        }
        return size
    }

    fun deleteFile(file: File): Boolean {
        if (file.isFile)
            return file.delete()
        var flag: Boolean = true
        val files = file.listFiles()
        files.forEach {
            flag = flag and deleteFile(it)
        }
        flag = flag and file.delete()
        return flag
    }

    //获取缓存目录
    fun getCache(context: Context): File {
        return context.getExternalCacheDir()!!
    }

    //获取缓存目录大小
    fun getCacheSize(context: Context): String {
        return getFileSizeStr(getFolderSize(getCache(context)))
    }

    //删除缓存文件
    fun deleteCache(context: Context): Boolean {
        return deleteFile(getCache(context))
    }

    //获取内部图片文件夹路径
    fun getPrivateImageFile(context: Context): File {
        val file = File(getCache(context), "image")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    //获取裁剪后头像路径
    fun getCropImgFile(context: Context): File {
        return File(getPrivateImageFile(context), "crop.jpg")
    }

    //获取拍照图片路径
    fun getCameraFile(context: Context, fileName: String): File {
        return File(getPrivateImageFile(context), "$fileName.jpg")
    }

}