package com.dcdz.kaiyanforkotlin.ui.activity

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.api.UrlConstant
import com.dcdz.kaiyanforkotlin.base.BaseActivity
import com.dcdz.kaiyanforkotlin.bean.CategoryBean
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.ui.adapter.CategoryDetailAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.CategoryDetailContract
import com.dcdz.kaiyanforkotlin.ui.presenter.CategoryDetailPresenter
import com.dcdz.kaiyanforkotlin.utils.ImageLoaderUtil
import com.dcdz.kaiyanforkotlin.utils.ShareUtils
import com.dcdz.kaiyanforkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_category_detail.*

/**
 * Created by LJW on 2018/10/31.
 * 分类详情
 */
class CategoryDetailActivity : BaseActivity(), CategoryDetailContract.View {

    private var categoryData: CategoryBean? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { CategoryDetailPresenter() }
    private val mAdapter by lazy { CategoryDetailAdapter(this, itemList, R.layout.item_category_detail) }
    private var loadingMore = false //是否加载更多

    init {
        mPresenter.attachView(this)
    }

    override fun layoutId(): Int = R.layout.activity_category_detail

    override fun initData() {
        categoryData = intent.getSerializableExtra(UrlConstant.BUNDLE_CATEGORY_DATA) as CategoryBean?
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        //给ActionBar的左边加上一个返回的图标
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        //设置headerImage
        ImageLoaderUtil.LoadImage(this, categoryData?.headerImage!!, imageView)

        //设置headerImage上面的描述信息
        tv_category_desc.text ="#${categoryData?.description}#"

        //CollapsingToolbarLayout属性设置
        collapsing_toolbar_layout.title = categoryData?.name
        collapsing_toolbar_layout.setExpandedTitleColor(Color.WHITE)//还没收缩时状态下字体颜色
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.BLACK)//收缩后Toolbar上字体的颜色

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        //滑动到底部自动加载数据
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        //设置FloatingActionButton点击后一键分享
        fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                ShareUtils.shareText(this@CategoryDetailActivity, categoryData!!.bgPicture)
            }
        })

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
    }

    /**
     * 初始化数据
     */
    override fun start() {
        categoryData?.id?.let {
            mPresenter.loadCategoryDetailList(it)
        }
    }

    /**
     * 获取到数据之后的回调
     */
    override fun showCategoryDetailData(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mAdapter.addData(itemList)
    }

    override fun showErrorInfo(errorMsg: String) {
        multipleStatusView.showError()
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
