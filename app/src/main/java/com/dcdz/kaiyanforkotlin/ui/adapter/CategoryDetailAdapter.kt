package com.dcdz.kaiyanforkotlin.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.api.UrlConstant
import com.dcdz.kaiyanforkotlin.base.BaseAdapter
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.ui.activity.VideoDetailActivity
import com.dcdz.kaiyanforkotlin.utils.ImageLoaderUtil
import com.dcdz.kaiyanforkotlin.utils.durationFormat
import com.tencent.bugly.proguard.t

/**
 * Created by LJW on 2018/10/31.
 * 分类详情
 */
class CategoryDetailAdapter(context: Context, dataList: ArrayList<HomeBean.Issue.Item>, layoutId: Int)
    : BaseAdapter<HomeBean.Issue.Item>(context, dataList, layoutId){

    fun addData(dataList: ArrayList<HomeBean.Issue.Item>) {
//        this.mData.clear() //添加这句之后，加载更多会出现闪的情况
        this.mData.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        setVideoItem(holder, data)
    }

    /**
     * 加载条目
     */
    private fun setVideoItem(holder: ViewHolder, item: HomeBean.Issue.Item) {
        val itemData = item.data
        val cover = itemData?.cover?.feed?:""
        // 加载封页图
        ImageLoaderUtil.LoadImage(mContext, cover, holder.getView(R.id.iv_image))
        holder.setText(R.id.tv_title, itemData?.title?:"")

        // 格式化时长
        val timeFormat = durationFormat(itemData?.duration)
        holder.setText(R.id.tv_tag, "#${itemData?.category}/$timeFormat")

        holder.setOnItemClickListener(listener = View.OnClickListener {
            goToVideoPlayer(mContext as Activity, holder.getView(R.id.iv_image), item)
        })
    }

    /**
     * 跳转到视频详情页面播放
     *
     * @param activity
     * @param view
     */
    private fun goToVideoPlayer(activity: Activity, view: View, itemData: HomeBean.Issue.Item) {
        val intent = Intent(activity, VideoDetailActivity::class.java)
        intent.putExtra(UrlConstant.BUNDLE_VIDEO_DATA, itemData)
        intent.putExtra(VideoDetailActivity.Companion.TRANSITION, true)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val pair = Pair<View, String>(view, VideoDetailActivity.IMG_TRANSITION)
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair)
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
        } else {
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
        }
    }
}