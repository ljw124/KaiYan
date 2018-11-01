package com.dcdz.kaiyanforkotlin.ui.activity

import android.annotation.TargetApi
import android.graphics.Typeface
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.dcdz.kaiyanforkotlin.MyApplication
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseActivity
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.ErrorStatus
import com.dcdz.kaiyanforkotlin.ui.adapter.CategoryDetailAdapter
import com.dcdz.kaiyanforkotlin.ui.adapter.HotKeywordsAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.SearchContract
import com.dcdz.kaiyanforkotlin.ui.presenter.SearchPresenter
import com.dcdz.kaiyanforkotlin.utils.StatusBarUtil
import com.dcdz.kaiyanforkotlin.utils.showToast
import com.dcdz.kaiyanforkotlin.view.ViewAnimUtils
import com.dcdz.kaiyanforkotlin.view.ViewAnimUtils.animateRevealShow
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), SearchContract.View {

    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { SearchPresenter() }
    private val mResultAdapter by lazy { CategoryDetailAdapter(this, itemList, R.layout.item_category_detail) }
    private var mHotKeywordsAdapter : HotKeywordsAdapter? = null
    private var keyWords: String? = null //关键词
    private var loadingMore = false //是否加载更多
    private var mTextTypeface: Typeface? = null

    init {
        mPresenter.attachView(this)
        mTextTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    override fun layoutId(): Int = R.layout.activity_search

    /**
     * 设置动画
     */
    override fun initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpEnterAnimation() // 入场动画
            setUpExitAnimation() // 退场动画
        } else {
            setUpView()
        }
    }

    override fun initView() {
        tv_hot_search_words.typeface = mTextTypeface

        //查询结果的RecyclerView
        mRecyclerView_result.layoutManager = LinearLayoutManager(this)
        mRecyclerView_result.adapter = mResultAdapter
        //滑动到底部时自动加载更多
        mRecyclerView_result.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView_result.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView_result.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        //点击“取消”
        tv_cancel.setOnClickListener{
            onBackPressed()
        }

        //监听键盘上的搜索按钮(说明：imeOptions可以用来设置输入法软键盘右下角的图标)
        et_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    closeSoftKeyboard()
                    keyWords = et_search_view.text.toString().trim()
                    if (keyWords.isNullOrEmpty()){
                        showToast("请输入你感兴趣的关键词")
                    } else {
                        mPresenter.loadSearchData(keyWords!!)
                    }
                }
                return false
            }
        })
        mLayoutStatusView = multipleStatusView

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
    }

    /**
     * 获取热门关键词
     */
    override fun start() {
        mPresenter.loadHotWordData()
    }

    /**
     * 获取到热门关键词后的回调
     */
    override fun setHotWord(hotWord: ArrayList<String>) {
        layout_hot_words.visibility = View.VISIBLE
        layout_content_result.visibility = View.GONE
        mHotKeywordsAdapter = HotKeywordsAdapter(this, hotWord, R.layout.item_flow_text)

        //设置流式布局
        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP      //按正常方向换行
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW   //主轴为水平方向，起点在左端
        flexBoxLayoutManager.alignItems = AlignItems.CENTER    //定义项目在副轴轴上如何对齐
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START  //多个轴对齐方式
        //热门关键词的RecyclerView
        mRecyclerView_hot.layoutManager = flexBoxLayoutManager
        mRecyclerView_hot.adapter = mHotKeywordsAdapter
        //设置 Item 的点击事件
        mHotKeywordsAdapter?.setOnTagItemClickListener {
            closeSoftKeyboard()
            keyWords = it
            mPresenter.loadSearchData(it)
        }
    }

    /**
     * 搜索结果的回调
     */
    override fun setSearchResult(issue: HomeBean.Issue) {
        loadingMore = false
        //隐藏关键词
        hideHotWordView()
        //更新条数提示
        tv_search_count.visibility = View.VISIBLE
        tv_search_count.text = String.format(resources.getString(R.string.search_result_count), keyWords, issue.total)

        itemList = issue.itemList
        mResultAdapter.addData(issue.itemList)
    }

    override fun closeSoftKeyboard() {
        closeKeyBord(et_search_view, applicationContext)
    }

    /**
     * 没用搜索到内容
     */
    override fun setEmptyView() {
        showToast("抱歉，没有搜索到内容")
        hideHotWordView()
        tv_search_count.visibility = View.GONE
        mLayoutStatusView?.showEmpty()
    }

    override fun showErrorInfo(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }

    /**
     * 退场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExitAnimation() {
        val fade = Fade()
        window.returnTransition = fade
        fade.duration = 300
    }

    /**
     * 进场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpEnterAnimation() {
        val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }
        })
    }

    /**
     * 展示动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow() {
        ViewAnimUtils.animateRevealShow(
                this, rel_frame,
                fab_circle.width / 2, R.color.backgroundColor,
                object : ViewAnimUtils.OnRevealAnimationListener {
                    override fun onRevealHide() {

                    }

                    override fun onRevealShow() {
                        setUpView()
                    }
                })
    }

    private fun setUpView() {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animation.duration = 300
        rel_container.startAnimation(animation)
        rel_container.visibility = View.VISIBLE
        //打开软键盘
        openKeyBord(et_search_view, applicationContext)
    }

    //隐藏热门关键字的 View
    private fun hideHotWordView(){
        layout_hot_words.visibility = View.GONE
        layout_content_result.visibility = View.VISIBLE
    }

    // 点击“取消”后，返回
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimUtils.animateRevealHide(
                    this, rel_frame,
                    fab_circle.width / 2, R.color.backgroundColor,
                    object : ViewAnimUtils.OnRevealAnimationListener {
                        override fun onRevealHide() {
                            defaultBackPressed()
                        }

                        override fun onRevealShow() {

                        }
                    })
        } else {
            defaultBackPressed()
        }
    }
    // 默认回退
    private fun defaultBackPressed() {
        closeSoftKeyboard()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        mTextTypeface = null
    }
}
