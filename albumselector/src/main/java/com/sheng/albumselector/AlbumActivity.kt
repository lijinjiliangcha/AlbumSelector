package com.sheng.albumselector

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : AppCompatActivity() {

    private val context: Context by lazy { this }
    private val permissionCode = 100

    companion object {
        val REQUEST_CODE: Int = 2019
        val SPAN_COUNT = "SPAN_COUNT"
        val MAX = "MAX"
        fun startActivity(activity: Activity, spanCount: Int, max: Int) {
            val intent = Intent(activity, AlbumActivity::class.java)
            intent.putExtra(SPAN_COUNT, spanCount)
            intent.putExtra(MAX, max)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        getSupportActionBar()?.hide()
        //状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(context.resources.getColor(R.color.titleColor));
        }
        //申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                permissionCode)
        } else
            init()
    }

    private fun init() {
        initView()
        initData()
        initAdapter()
        initListener()
    }

    private fun initView() {
        val spanCount = intent.getIntExtra(SPAN_COUNT, 0)
        val max = intent.getIntExtra(MAX, 1)
        rv.layoutManager = GridLayoutManager(context, spanCount)
        val adapter = AlbumAdapter(this)
        rv.adapter = adapter
        adapter.setSelectCount(max)
    }

    private fun initData() {

    }

    private fun initAdapter() {
//        adapter = AlbumAdapter(this, imageList)
        //获取选中的图片
//        var imgData = intent.getStringExtra("data")
//        if (imgData != null) {
//            var list: ArrayList<String> = Gson().fromJson(imgData, object : TypeToken<ArrayList<String>>() {}.type)
//            list.forEachIndexed { index, s ->
//                adapter?.addSelect(s)
//            }
//        }
//        rv.adapter = adapter
    }

    private fun initListener() {
//        adapter?.setOnItemClickListener(object : AlbumAdapter.OnItemClickListener {
//            override fun onAddClickListener() {
//                cameraFile = File(Utils.getFilePath(), "zamo-${System.currentTimeMillis()}.jpg")
//                startActivityForResult(Utils.getCameraIntent(context, cameraFile!!), 100)
//            }
//        })
//        //确定
//        tv_comfit.setOnClickListener {
//            var list = adapter?.getSelect()
//            var intent = Intent()
//            intent.putExtra("data", Gson().toJson(list))
//            setResult(Activity.RESULT_OK, intent)
//            finish()
//        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            //判断一下定位权限
            for (index in 0 until permissions.size) {
                val str = permissions[index]
                if (str.equals(Manifest.permission.READ_EXTERNAL_STORAGE))
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        init()
                        return
                    }
            }
            finish()
        }
    }

}