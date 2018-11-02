package com.dcdz.kaiyanforkotlin.ui.activity

import android.annotation.TargetApi
import android.content.res.Configuration
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.api.UrlConstant
import com.dcdz.kaiyanforkotlin.base.BaseActivity
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.ui.adapter.VideoDetailAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.VideoDetailContract
import com.dcdz.kaiyanforkotlin.ui.presenter.VideoDetailPresenter
import com.dcdz.kaiyanforkotlin.utils.ImageLoaderUtil
import com.dcdz.kaiyanforkotlin.utils.StatusBarUtil
import com.dcdz.kaiyanforkotlin.utils.showToast
import com.dcdz.kaiyanforkotlin.view.VideoListener
import com.scwang.smartrefresh.header.MaterialHeader
import com.shuyu.gsyvideoplayer.listener.LockClickListener
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_detail.*
import org.apache.log4j.Logger

class VideoDetailActivity : BaseActivity(), VideoDetailContract.View {

    //用于指定过渡动画的协作元素
    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
    }

    private lateinit var itemData: HomeBean.Issue.Item
    private var orientationUtils: OrientationUtils? = null //处理屏幕旋转的的逻辑
    private var itemList = java.util.ArrayList<HomeBean.Issue.Item>()

    private var isPlay: Boolean = false //是否播放
    private var isPause: Boolean = false //是否暂停

    private var isTransition: Boolean = false

    private var transition: Transition? = null
    private var mMaterialHeader: MaterialHeader? = null

    //第一次调用时初始化
    private val mPresenter : VideoDetailPresenter by lazy { VideoDetailPresenter() }
    private val mAdapter : VideoDetailAdapter by lazy { VideoDetailAdapter(this, itemList) }


    override fun layoutId(): Int = R.layout.activity_video_detail

    override fun initView() {
        mPresenter.attachView(this)
        initTransition()
        initVideoViewConfig()

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        //设置相关视频的点击事件
        mAdapter.setOnItemDetailClick { mPresenter.loadVideoInfo(it) }

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, mVideoView)

        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        //下拉刷新
        mRefreshLayout.setOnRefreshListener {
            loadVideoInfo()
        }
        //下拉刷新头部
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域块背景:
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)
    }

    /**
     * 初始化数据
     */
    override fun initData() {
        itemData = intent.getSerializableExtra(UrlConstant.BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
        isTransition = intent.getBooleanExtra(TRANSITION, false)

        saveWatchVideoHistoryInfo(itemData)
    }

    override fun start() {
    }

    /**
     * 设置播放视频 URL
     */
    override fun setVideo(url: String) {
        log.info("playUrl:$url")
        mVideoView.setUp(url,false,"") //设置播放URL
        //开始播放
        mVideoView.startPlayLogic()
    }

    /**
     * 设置视频信息
     */
    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {
        itemData = itemInfo
        mAdapter.addData(itemInfo)
        //请求最新的相关视频
        mPresenter.loadRelatedVideo(itemInfo.data?.id?:0)
    }

    /**
     * 设置背景颜色
     */
    override fun setBackground(url: String) {
        ImageLoaderUtil.LoadImage(this, url!!, mVideoBackground)
    }

    /**
     * 设置相关的数据视频
     */
    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        this.itemList = itemList
        mAdapter.addData(itemList)
    }

    override fun setErrorMsg(errorMsg: String) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause){
            mVideoView.onConfigurationChanged(this, newConfig, orientationUtils)
        }
    }

    private fun initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //delay starting the entering and shared element transitions until all data is loaded
            postponeEnterTransition() //数据加载完之后，延迟进入
            ViewCompat.setTransitionName(mVideoView, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            loadVideoInfo()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener() {
        transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
            }

            override fun onTransitionEnd(p0: Transition?) {
                log.info("onTransitionEnd()------")
                loadVideoInfo()
                transition?.removeListener(this)
            }

        })
    }

    /**
     * 加载视频信息
     */
    fun loadVideoInfo() {
        mPresenter.loadVideoInfo(itemData)
    }

    /**
     * 初始化 VideoView 的配置
     */
    private fun initVideoViewConfig() {
        //设置旋转
        orientationUtils = OrientationUtils(this, mVideoView)
        //是否旋转
        mVideoView.isRotateViewAuto = false
        //是否可以滑动调整
        mVideoView.setIsTouchWiget(true)

        //设置封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoaderUtil.LoadImage(this, itemData.data?.cover?.feed!!, imageView)
        mVideoView.thumbImageView = imageView

        //设置监听
        mVideoView.setStandardVideoAllCallBack(object : VideoListener {
            override fun onPrepared(url: String, vararg objects: Any) {
                super.onPrepared(url, *objects)
                //开始播放之后可以旋转、全屏
                orientationUtils?.isEnable = true
                isPlay = true
            }

            override fun onAutoComplete(url: String, vararg objects: Any) {
                super.onAutoComplete(url, *objects)
                log.info("***** onAutoPlayComplete ****")
            }

            override fun onPlayError(url: String, vararg objects: Any) {
                super.onPlayError(url, *objects)
                showToast("播放失败")
                log.error("播放失败")
            }

            override fun onEnterFullscreen(url: String, vararg objects: Any) {
                super.onEnterFullscreen(url, *objects)
                log.info("***** onEnterFullscreen **** ")
            }

            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                super.onQuitFullscreen(url, *objects)
                log.info("***** onQuitFullscreen **** ")
                //列表返回的样式判断
                orientationUtils?.backToProtVideo()
            }
        })
        //设置返回按键功能
        mVideoView.backButton.setOnClickListener({onBackPressed()})
        //设置全屏按键功能
        mVideoView.fullscreenButton.setOnClickListener{
            //横屏
            orientationUtils?.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            mVideoView.startWindowFullscreen(this, true, true)
        }
        //锁屏事件
        mVideoView.setLockClickListener(object : LockClickListener{
            override fun onClick(view: View?, lock: Boolean) {
                //配合下方的onConfigurationChanged
                orientationUtils?.isEnable = !lock
            }
        })
    }

    /**
     * 保存观看记录
     */
    private fun saveWatchVideoHistoryInfo(watchItem: HomeBean.Issue.Item) {
        //保存之前要先查询sp中是否有该value的记录，有则删除.这样保证搜索历史记录不会有重复条目
//        val historyMap = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME, MyApplication.context) as Map<*, *>
//        for ((key, _) in historyMap) {
//            if (watchItem == WatchHistoryUtils.getObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context, key as String)) {
//                WatchHistoryUtils.remove(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context, key)
//            }
//        }
//        WatchHistoryUtils.putObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context, watchItem,"" + mFormat.format(Date()))
    }

    /**
     * 监听返回键
     */
    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (StandardGSYVideoPlayer.backFromWindowFull(this))
            return
        //释放所有
        mVideoView.setStandardVideoAllCallBack(null)
        GSYVideoPlayer.releaseAllVideos()
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) run {
            super.onBackPressed()
        } else {
            finish()
            overridePendingTransition(R.anim.anim_out, R.anim.anim_in)
        }
    }

    private fun getCurPlay(): GSYVideoPlayer {
        return if (mVideoView.fullWindowPlayer != null) {
            mVideoView.fullWindowPlayer
        } else mVideoView
    }

    override fun onResume() {
        super.onResume()
        getCurPlay().onVideoResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        getCurPlay().onVideoPause()
        isPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.releaseListener()
        mPresenter.detachView()
    }
}
