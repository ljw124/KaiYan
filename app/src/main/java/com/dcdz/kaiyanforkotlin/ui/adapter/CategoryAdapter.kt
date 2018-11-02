package com.dcdz.kaiyanforkotlin.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dcdz.kaiyanforkotlin.MyApplication
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.api.UrlConstant
import com.dcdz.kaiyanforkotlin.base.BaseAdapter
import com.dcdz.kaiyanforkotlin.bean.CategoryBean
import com.dcdz.kaiyanforkotlin.ui.activity.CategoryDetailActivity
import com.dcdz.kaiyanforkotlin.utils.ImageLoaderUtil
import com.tencent.bugly.proguard.u
import java.net.URLConnection

/**
 * Created by LJW on 2018/10/31.
 */
class CategoryAdapter(context: Context, categoryList: ArrayList<CategoryBean>, layoutId: Int) :
    BaseAdapter<CategoryBean>(context, categoryList, layoutId){

    //设置字体
    private var textTypeface: Typeface?=null
    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
    }

    /**
     * 设置新数据
     */
    fun setData(categoryList: ArrayList<CategoryBean>){
        mData.clear()
        mData = categoryList
        notifyDataSetChanged()
    }

    override fun bindData(holder: ViewHolder, data: CategoryBean, position: Int) {
        //设置方正兰亭细黑简体
        holder.getView<TextView>(R.id.tv_category_name).typeface = textTypeface
        //设置类别
        holder.setText(R.id.tv_category_name, "#${data.name}")
        //设置背景图
        holder.setImagePath(R.id.iv_category, object : ViewHolder.HolderImageLoader(data.bgPicture){
            override fun loadImage(iv: ImageView, path: String) {
                ImageLoaderUtil.LoadImage(mContext, path, iv)
            }
        })
        //设置条目点击事件
        holder.setOnItemClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(mContext as Activity, CategoryDetailActivity::class.java)
                intent.putExtra(UrlConstant.BUNDLE_CATEGORY_DATA,data)
                mContext.startActivity(intent)
            }
        })
    }
}