package com.sheng.albumselector

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions

class AlbumAdapter : RecyclerView.Adapter<AlbumHolder> {

    private val context: Context
    private val dataList: ArrayList<String>
    private val selectList = ArrayList<String>()
    private val holderList = ArrayList<AlbumHolder>()//保存勾选的holder
    private var max = 1

    constructor(context: Context) : super() {
        this.context = context
        dataList = Utils.getImagesMedia2(context)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return AlbumHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        val path = dataList[position]
        loadImg(path, holder.iv_img)
        holder.path = path
        //回显选中
        val index = selectList.indexOf(path)
        if (index != -1) {
            holder.isSelect = true
            holder.tv_select.text = (index + 1).toString()
        } else {
            holder.isSelect = false
            holder.tv_select.text = ""
        }
        //点击事件
        holder.itemView.setOnClickListener {
            if (holder.isSelect) {//选中->取消
                holder.isSelect = false
                selectList.remove(path)
                holderList.remove(holder)
                refreshSelect()
//                notifyDataSetChanged()
            } else {//未选中->选中
                //TODO - 单选模式
                //多选模式
                if (selectList.size >= max) {
                    Toast.makeText(context, "最多只能选择${max}张图片", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    holder.isSelect = true
                    selectList.add(path)
                    holder.tv_select.text = selectList.size.toString()
                    holderList.add(holder)
                }
            }
        }
    }

    //刷新指示器数字 - 避免图片闪烁
    private fun refreshSelect() {
        holderList.forEach {
            val index = selectList.indexOf(it.path)
            Log.i("测试", index.toString())
            if (index != -1)
                it.tv_select.text = (index + 1).toString()
        }
    }

    fun setSelect(list: List<String>?) {
        selectList.clear()
        if (list != null)
            selectList.addAll(list)
        notifyDataSetChanged()
    }

    fun getSelect(): ArrayList<String> {
        return selectList
    }

    fun setSelectCount(count: Int) {
        max = count
    }

    private fun loadImg(path: String, imageView: ImageView) {
        val optionsCacheNone = RequestOptions()
            //指定加载的优先级，优先级越高越优先加载，
            .format(DecodeFormat.PREFER_RGB_565)
            .priority(Priority.NORMAL)
            .dontAnimate()
            .centerCrop()

        Glide.with(context)
            .load(path)
            .thumbnail(0.85f)
            .apply(optionsCacheNone)
            .into(imageView)
    }

    // 设置列表数据并刷新
    fun setData(list: ArrayList<String>){
        clear()
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    // 清除已经选择等缓存、标记，避免意外
    fun clear(){
        holderList.clear()
        selectList.clear()
    }

}