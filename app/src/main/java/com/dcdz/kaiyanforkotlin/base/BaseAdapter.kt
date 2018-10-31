package com.dcdz.kaiyanforkotlin.base

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.apache.log4j.Logger

/**
 * Created by LJW on 2018/10/23.
 * 通用的 Adapter
 */
abstract class BaseAdapter<T>(var mContext: Context, var mData: ArrayList<T>, //条目布局
                              private var mLayoutId: Int) : RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    protected var mInflater: LayoutInflater? = null
    private var mTypeSupport: MultipleType<T>? = null
    internal var log = Logger.getLogger(BaseAdapter::class.java!!)

    //使用接口回调点击事件
    private var mItemClickListener: OnItemClickListener? = null

    //使用接口回调点击事件
    private var mItemLongClickListener: OnItemLongClickListener? = null

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    //需要多布局
    constructor(context: Context, data: ArrayList<T>, typeSupport: MultipleType<T>) : this(context, data, -1) {
        this.mTypeSupport = typeSupport
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mTypeSupport != null) {
            //需要多布局
            mLayoutId = viewType
        }
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return ViewHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        //多布局问题
        return mTypeSupport?.getLayoutId(mData[position], position) ?: super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //绑定数据
        bindData(holder, mData[position], position)

//        if (mItemClickListener != null) {
//            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(mData[position], position) }
//        }

//        if (mItemLongClickListener != null) {
//            holder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(mData[position], position) }
//        }
        //条目点击事件
        mItemClickListener?.let {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(mData[position], position) }
        }

        //长按点击事件
        mItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(mData[position], position) }
        }
    }

    /**
     * 将必要参数传递出去
     */
    protected abstract fun bindData(holder: ViewHolder, data: T, position: Int)

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener
    }

    //点击事件
    interface OnItemClickListener {
        fun onItemClick(obj: Any?, position: Int)
    }
    //长按事件
    interface OnItemLongClickListener {
        fun onItemLongClick(obj: Any?, position: Int): Boolean
    }
    //多布局
    interface MultipleType<in T> {
        fun getLayoutId(item: T, position: Int): Int
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //用于缓存已有找的界面
        private var mView: SparseArray<View>?=null

        init {
            mView = SparseArray()
        }

        fun <T : View> getView(viewId: Int): T {
            //对已有的view做缓存
            var view: View? = mView?.get(viewId)
            //使用缓存的方式减少findViewById的次数
            if (view == null) {
                view = itemView.findViewById(viewId)
                mView?.put(viewId, view)
            }
            return view as T
        }

        fun <T : ViewGroup> getViewGroup(viewId: Int): T {
            //对已有的view做缓存
            var view: View? = mView?.get(viewId)
            //使用缓存的方式减少findViewById的次数
            if (view == null) {
                view = itemView.findViewById(viewId)
                mView?.put(viewId, view)
            }
            return view as T
        }

        @SuppressLint("SetTextI18n")
        //通用的功能进行封装  设置文本 设置条目点击事件  设置图片
        fun setText(viewId: Int, text: CharSequence): ViewHolder {
            val view = getView<TextView>(viewId)
            view.text = "" + text
            //希望可以链式调用
            return this
        }

        fun setHintText(viewId: Int, text: CharSequence): ViewHolder {
            val view = getView<TextView>(viewId)
            view.hint = "" + text
            return this
        }

        /**
         * 设置本地图片
         */
        fun setImageResource(viewId: Int, resId: Int): ViewHolder {
            val iv = getView<ImageView>(viewId)
            iv.setImageResource(resId)
            return this
        }

        /**
         * 加载图片资源路径
         */
        fun setImagePath(viewId: Int, imageLoader: HolderImageLoader): ViewHolder {
            val iv = getView<ImageView>(viewId)
            imageLoader.loadImage(iv, imageLoader.path)
            return this
        }

        abstract class HolderImageLoader(val path: String) {

            /**
             * 需要去复写这个方法加载图片
             */
            abstract fun loadImage(iv: ImageView, path: String)
        }

        /**
         * 设置View的Visibility
         */
        fun setViewVisibility(viewId: Int, visibility: Int): ViewHolder {
            getView<View>(viewId).visibility = visibility
            return this
        }

        /**
         * 设置条目点击事件
         */
        fun setOnItemClickListener(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        /**
         * 设置条目长按事件
         */
        fun setOnItemLongClickListener(listener: View.OnLongClickListener) {
            itemView.setOnLongClickListener(listener)
        }
    }
}
