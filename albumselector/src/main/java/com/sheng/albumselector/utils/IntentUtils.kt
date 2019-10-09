package com.sheng.albumselector.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

object IntentUtils {

    //调用系统相册
    fun getAlbumIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        return intent
    }

    //调用系统相机
    fun getCameraIntent(context: Context, fileName: String): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(context, FileUtils.getCameraFile(context, fileName)))
        return intent
    }

    //调用系统裁剪
    fun getCropPhotoIntent(context: Context, uri: Uri): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500)
        intent.putExtra("outputY", 500)
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(context, FileUtils.getCropImgFile(context)));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtils.getCropImgFile(context)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent
    }

    //调用系统拨号盘
    fun callPhone(tel: String): Intent {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    fun getUri(context: Context, file: File): Uri {
        val uri: Uri
        if (Build.VERSION.SDK_INT >= 24) {
            val authority: String = context.packageName + ".file.MyFileProvider"
            Log.i("测试","authority = $authority")
            uri = FileProvider.getUriForFile(context, authority, file)
        } else {
            uri = Uri.fromFile(file)
        }
        return uri
    }

}