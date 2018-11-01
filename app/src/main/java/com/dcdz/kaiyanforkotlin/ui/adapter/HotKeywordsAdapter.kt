package com.dcdz.kaiyanforkotlin.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseAdapter
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * Created by LJW on 2018/11/1.
 * 热门搜索词
 */
class HotKeywordsAdapter(context: Context, words: ArrayList<String>, layoutId: Int):
        BaseAdapter<String>(context, words, layoutId){

    /**
     * Kotlin的函数可以作为参数，写callback的时候，可以不用interface了
     */
    private var mOnTagItemClick: ((tag:String) -> Unit)? = null

    fun setOnTagItemClickListener(onTagItemClickListener:(tag:String) -> Unit) {
        this.mOnTagItemClick = onTagItemClickListener
    }

    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        holder.setText(R.id.tv_title,data)

        val params = holder.getView<TextView>(R.id.tv_title).layoutParams
        if(params is FlexboxLayoutManager.LayoutParams){
            params.flexGrow = 1.0f
        }

        holder.setOnItemClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                mOnTagItemClick?.invoke(data)
            }
        }
        )
    }
}