package com.dcdz.kaiyanforkotlin.ui.fragment


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.ErrorStatus
import com.dcdz.kaiyanforkotlin.ui.activity.SearchActivity
import com.dcdz.kaiyanforkotlin.ui.adapter.HomeAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.HomeContract
import com.dcdz.kaiyanforkotlin.ui.presenter.HomePresenter
import com.dcdz.kaiyanforkotlin.utils.StatusBarUtil
import com.dcdz.kaiyanforkotlin.utils.showToast
import com.scwang.smartrefresh.header.MaterialHeader
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment(), HomeContract.View {


    private val mPresenter by lazy { HomePresenter() }
    private var mTitle: String? = null
    private var num: Int = 1
    private var loadingMore = false
    private var isRefresh = false
    private var mMaterialHeader: MaterialHeader? = null
    private var mHomeAdapter: HomeAdapter? = null

    companion object {
        fun getInstance(title: String): HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MMM. dd, 'Brunch' -", Locale.CHINA)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        //绑定view， presenter中的mRootView便是此view
        mPresenter.attachView(this)
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        //刷新数据
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            mPresenter.loadHomeData(num)
        }
        //下拉刷新头部
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域背景
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)

        //RecyclerView设置滑动监听(注意：布局中禁止了RefreshLayout的加载更多，是为了处理滑动过程中Toolbar的显隐)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    var childCount = mRecyclerView.childCount
                    var itemCount = mRecyclerView.layoutManager.itemCount
                    var firstVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    //滑动到底部，加载更多
                    if (firstVisibleItem + childCount == itemCount){
                        if (!loadingMore){
                            loadingMore = true
                            mPresenter.loadMoreData()
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //第一个可见view的位置
                val currentVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                if (currentVisibleItemPosition == 0) { //滑动到了第一个元素，Toolbar设置透明
                    //背景设置为透明
                    toolbar.setBackgroundColor(getColor(R.color.color_translucent))
                    tv_header_title.text = ""
                    iv_search.setImageResource(R.mipmap.ic_action_search_white)
                } else {
                    if (mHomeAdapter?.mData!!.size > 1) {
                        toolbar.setBackgroundColor(getColor(R.color.color_title_bg))
                        val itemList = mHomeAdapter!!.mData
                        val item = itemList[currentVisibleItemPosition + mHomeAdapter!!.bannerItemSize - 1]
                        if (item.type == "textHeader") { //判断是否日期 如：-Oct.28,Brunch-
                            tv_header_title.text = item.data?.text
                        } else {
                            tv_header_title.text = simpleDateFormat.format(item.data?.date)
                        }
                    }
                }
            }
        })

        //搜索按钮设置监听
        iv_search.setOnClickListener { openSearchActivity() }

        //设置多布局
        mLayoutStatusView = multipleStatusView

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }
    }

    /**
     * 初始化数据
     */
    override fun lazyLoad() {
        mPresenter.loadHomeData(num)
    }

    /**
     * 获取到首页数据后的回调
     */
    override fun setHomeData(homeBean: HomeBean) {
        mLayoutStatusView?.showContent()
        //给adapter设置数据
        mHomeAdapter = activity?.let {
            HomeAdapter(it, homeBean.issueList[0].itemList)
        }
        //设置banner大小
        mHomeAdapter?.setBannerSize(homeBean.issueList[0].count)

        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator() //默认动画
    }

    /**
     * 加载更多后的回调
     */
    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mHomeAdapter?.addItemData(itemList)
    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }

    override fun showLoading() {
        if (!isRefresh){
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }

    override fun dismissLoading() {
        mRefreshLayout?.finishRefresh()
    }

    private fun openSearchActivity() {
        //如果SDK版本不小于21，使用过渡动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //场景动画(第三个参数指定哪两个view进行协作)  https://blog.csdn.net/qibin0506/article/details/48129139
            val options = activity?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(it, iv_search, iv_search.transitionName) }
            startActivity(Intent(activity, SearchActivity::class.java), options?.toBundle())
        } else {
            startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    fun getColor(colorId: Int): Int {
        return resources.getColor(colorId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
