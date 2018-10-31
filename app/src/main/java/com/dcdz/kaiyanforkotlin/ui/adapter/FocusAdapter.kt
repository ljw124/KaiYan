package com.dcdz.kaiyanforkotlin.ui.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseAdapter
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.utils.ImageLoaderUtil

/**
 * Created by LJW on 2018/10/31.
 * 关注
 */
class FocusAdapter(context: Context, dataList: ArrayList<HomeBean.Issue.Item>)
    : BaseAdapter<HomeBean.Issue.Item>(context, dataList, object: MultipleType<HomeBean.Issue.Item>{
    override fun getLayoutId(item: HomeBean.Issue.Item, position: Int): Int {
        return when{
            item.type == "videoCollectionWithBrief" ->
                R.layout.item_focus
            else ->
                throw IllegalAccessException("数据解析出错了，出现其他类型")
        }
    }
}){

    fun addData(dataList: ArrayList<HomeBean.Issue.Item>) {
        this.mData.addAll(dataList)
        notifyDataSetChanged()
    }

    /**
     * 绑定数据
     */
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when {
            data.type == "videoCollectionWithBrief" -> setAuthorInfo(data, holder)
        }
    }

    /**
     * 设置作者信息
     */
    private fun setAuthorInfo(item: HomeBean.Issue.Item, holder: ViewHolder){
        val headerData = item.data?.header
        //加载作者头像
        holder.setImagePath(R.id.iv_avatar, object : ViewHolder.HolderImageLoader(headerData?.icon!!){
            override fun loadImage(iv: ImageView, path: String) {
                ImageLoaderUtil.LoadImage(mContext, path, holder.getView(R.id.iv_avatar))
            }
        })
        //标题
        holder.setText(R.id.tv_title, headerData.title)
        //描述信息
        holder.setText(R.id.tv_desc, headerData.description)
        //设置RecyclerView为水平方向
        val recyclerView = holder.getView<RecyclerView>(R.id.fl_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext as Activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = FocusHorizontalAdapter(mContext, item.data.itemList, R.layout.item_focus_horizontal)
    }
}