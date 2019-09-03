package com.sheng.albumselector.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import com.sheng.albumselector.*
import com.sheng.albumselector.adpater.AlbumAdapter
import com.sheng.albumselector.callback.AlbumLoaderCallbacks
import com.sheng.albumselector.widget.AlbumIndexPopupWindow
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : AppCompatActivity(), LifecycleOwner {

    private val context: Context by lazy { this }
    private val permissionCode = 100

    private val adapter by lazy { AlbumAdapter(context) }
    private val mAlbumPop by lazy { AlbumIndexPopupWindow(context) }// 相册列表弹窗
    private val mAlbumLoaderCallbacks by lazy {
        AlbumLoaderCallbacks(
            this
        )
    }// 相册加载的回调

    private val URL_LOADER = 402

    companion object {
        val REQUEST_CODE: Int = 2019
        val SPAN_COUNT = "SPAN_COUNT"
        val MAX = "MAX"
        val SELECT_LIST = "SELECT_LIST"
        val RESULT_DATA = "RESULT_DATA"
        fun startActivity(activity: Activity, spanCount: Int, max: Int) {
            startActivity(
                activity,
                spanCount,
                max,
                null
            )
        }

        fun startActivity(activity: Activity, spanCount: Int, max: Int, selectList: ArrayList<String>?) {
            val intent = Intent(activity, AlbumActivity::class.java)
            intent.putExtra(SPAN_COUNT, spanCount)
            intent.putExtra(MAX, max)
            intent.putExtra(SELECT_LIST, selectList)
            activity.startActivityForResult(intent,
                REQUEST_CODE
            )
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
            mAlbumPop.show()
        }
        //分类选择器
        mAlbumPop.setClickItemChangeListener { it, _ ->
            tv_title.text = it
            val entity = mAlbumLoaderCallbacks.getData()[it]
            adapter.setData(entity)
            mAlbumPop.dismiss()
        }
        //加载数据回调
        mAlbumLoaderCallbacks.setLoadFinishedListener {
            val allText = mAlbumLoaderCallbacks.ALL
            val list = it[allText]
            adapter.setData(list)
            val albumNameList = it.map {
                it.key
            } as? ArrayList<String>
            albumNameList?.sortWith(AlbumComparator(allText))
            mAlbumPop.anchor = titleLayout
            mAlbumPop.setData(albumNameList!!)
        }
    }

    // 初始化数据加载
    private fun initLoader() {
        LoaderManager.getInstance(this).initLoader(URL_LOADER, null, mAlbumLoaderCallbacks)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            //判断一下读写权限
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