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

    private val adapter by lazy { AlbumAdapter(this) }

    companion object {
        val REQUEST_CODE: Int = 2019
        val SPAN_COUNT = "SPAN_COUNT"
        val MAX = "MAX"
        val SELECT_LIST = "SELECT_LIST"
        val RESULT_DATA = "RESULT_DATA"
        fun startActivity(activity: Activity, spanCount: Int, max: Int) {
            startActivity(activity, spanCount, max, null)
        }

        fun startActivity(activity: Activity, spanCount: Int, max: Int, selectList: ArrayList<String>?) {
            val intent = Intent(activity, AlbumActivity::class.java)
            intent.putExtra(SPAN_COUNT, spanCount)
            intent.putExtra(MAX, max)
            intent.putExtra(SELECT_LIST, selectList)
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
        initListener()
    }

    private fun initView() {
        val spanCount = intent.getIntExtra(SPAN_COUNT, 0)
        val max = intent.getIntExtra(MAX, 1)
        val list = intent.getStringArrayListExtra(SELECT_LIST)
        rv.layoutManager = GridLayoutManager(context, spanCount)

        adapter.setSelect(list)
        adapter.setSelectCount(max)
        rv.adapter = adapter
    }

    private fun initListener() {
        //确定
        tv_submit.setOnClickListener {
            val list = adapter.getSelect()
            val intent = Intent()
            intent.putExtra(RESULT_DATA, list)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        //取消
        tv_cancel.setOnClickListener {
            finish()
        }
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