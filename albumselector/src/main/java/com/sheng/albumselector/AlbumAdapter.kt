package com.sheng.albumselector

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.collection.ArraySet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions

class AlbumAdapter : RecyclerView.Adapter<AlbumHolder> {

    private val context: Context
    private val dataList: ArrayList<String>
    private val selectSet = ArraySet<String>()
    private var max = 1

    constructor(context: Context) : super() {
        this.context = context
        dataList = Utils.getImagesMedia(context)
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
        //回显选中
        holder.isSelect = selectSet.contains(path)
        //点击事件
        holder.itemView.setOnClickListener {
            if (holder.isSelect) {//选中->取消
                holder.isSelect = false
                selectSet.remove(path)
            } else {//未选中->选中
                //TODO - 单选模式
                //多选模式
                if (selectSet.size >= max) {
                    Toast.makeText(context, "最多只能选择${max}张图片", Toast.LENGTH_SHORT).show()
                } else {
                    holder.isSelect = true
                    selectSet.add(path)
                }
            }

        }
    }


    fun setSelect(set: ArraySet<String>) {
        selectSet.clear()
        selectSet.addAll(set)
        notifyDataSetChanged()
    }

    fun setSelect(list: List<String>) {
        selectSet.clear()
        list.forEach {
            selectSet.add(it)
        }
        notifyDataSetChanged()
    }

    fun getSelect(): ArraySet<String> {
        return selectSet
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
            .apply(optionsCacheNone)
            .into(imageView)
    }

}