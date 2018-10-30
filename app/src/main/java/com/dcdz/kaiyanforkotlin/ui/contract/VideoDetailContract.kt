package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean

/**
 * Created by LJW on 2018/10/29.
 */
interface VideoDetailContract {

    interface View : IBaseView {
        /**
         * 设置视频播放源
         */
        fun setVideo(url: String)

        /**
         * 设置视频信息
         */
        fun setVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 设置背景
         */
        fun setBackground(url: String)

        /**
         * 设置最新相关视频
         */
        fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * 设置错误信息
         */
        fun setErrorMsg(errorMsg: String)
    }

    interface Presenter: IPresenter<View> {

        /**
         * 加载视频资源
         */
        fun loadVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 加载相关的视频数据
         */
        fun loadRelatedVideo(id: Long)
    }
}