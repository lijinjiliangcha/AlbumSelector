package com.sheng.albumselector

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.sheng.albumselector.entity.AlbumEntity
import kotlinx.android.synthetic.main.activity_album.*
import java.io.File

class AlbumActivity : AppCompatActivity(), LifecycleOwner {

    private val context: Context by lazy { this }
    private val permissionCode = 100
    private val mAlbumPop by lazy { AlbumListPopupWindow(this) }

    private val adapter by lazy { AlbumAdapter(this) }
    private val mAlbumLoaderCallbacks by lazy { AlbumLoaderCallbacks(this) }

    private val URL_LOADER = 402

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
        initLoader()
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
        // 选择相册
        tv_title.setOnClickListener {
            val albumNameList = mAlbumLoaderCallbacks.getData().map { it.mName } as? ArrayList<String>
            if (albumNameList.isNullOrEmpty()) {
                Toast.makeText(this, "读取相册出错", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAlbumPop.anchorView = titleLayout
            mAlbumPop.setAdapter(albumNameList)
            mAlbumPop.setClickItemChangeListener { tv_title.text = it }
            mAlbumPop.show()
        }
    }

    // 初始化数据加载
    private fun initLoader() {
        lifecycle.addObserver(object : LifecycleObserver {})
        supportLoaderManager.initLoader(URL_LOADER, null, mAlbumLoaderCallbacks)
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