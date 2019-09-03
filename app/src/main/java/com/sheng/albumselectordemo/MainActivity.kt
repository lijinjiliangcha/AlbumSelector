package com.sheng.albumselectordemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sheng.albumselector.ui.AlbumActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            AlbumActivity.startActivity(this, 3, 20, selectList)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AlbumActivity.REQUEST_CODE)
            if (resultCode == Activity.RESULT_OK) {
                val list: ArrayList<String>? = data?.getStringArrayListExtra(AlbumActivity.RESULT_DATA)
                selectList = list
                list?.forEachIndexed { index, s ->
                    Log.i("测试", "$index = $s")
                }
            }
    }
}
