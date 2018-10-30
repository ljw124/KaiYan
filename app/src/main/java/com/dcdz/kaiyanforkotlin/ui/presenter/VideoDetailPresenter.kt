package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.MyApplication
import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.ExceptionHandle
import com.dcdz.kaiyanforkotlin.ui.contract.VideoDetailContract
import com.dcdz.kaiyanforkotlin.ui.model.VideoDetailModel
import com.dcdz.kaiyanforkotlin.utils.DisplayManager
import com.dcdz.kaiyanforkotlin.utils.NetworkUtil

/**
 * Created by LJW on 2018/10/29.
 */
class VideoDetailPresenter : BasePresenter<VideoDetailContract.View>(), VideoDetailContract.Presenter{

    private val videoDetailModel : VideoDetailModel by lazy {
        VideoDetailModel()
    }

    /**
     * 加载视频相关的数据
     */
    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {
        val playInfo = itemInfo.data?.playInfo
        //获取网络状态-确定播放哪种视频
        val netType = NetworkUtil.isWifi(MyApplication.context)
        //是否绑定了 View
        checkViewAttached()
        if (playInfo!!.size > 1){ //有的视频对应多个播放地址
            //WiFi 环境下播放高清视频
            if (netType){
                for (i in playInfo){
                    if (i.type == "high"){
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        break
                    }
                }
            } else {
                //否则播放标清视频
                for (i in playInfo){
                    if (i.type == "normal"){
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        break
                    }
                }
            }
        } else {
            mRootView?.setVideo(itemInfo.data.playUrl)
        }

        //设置背景
        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        backgroundUrl.let {
            mRootView?.setBackground(it)
        }

        //设置视频信息
        mRootView?.setVideoInfo(itemInfo)
    }

    /**
     * 请求相关的视频数据
     */
    override fun loadRelatedVideo(id: Long) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.requestRelatedData(id)
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        setRecentRelatedVideo(issue.itemList)
                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        setErrorMsg(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }
}