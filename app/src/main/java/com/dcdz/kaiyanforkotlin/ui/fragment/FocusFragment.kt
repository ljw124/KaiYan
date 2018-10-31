package com.dcdz.kaiyanforkotlin.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.ErrorStatus
import com.dcdz.kaiyanforkotlin.ui.adapter.FocusAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.FocusContract
import com.dcdz.kaiyanforkotlin.ui.presenter.FocusPresenter
import com.dcdz.kaiyanforkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_focus.*

/**
 * Created by LJW on 2018/10/30.
 * 关注
 */
class FocusFragment : BaseFragment(),FocusContract.View {

    private var mTitle: String? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { FocusPresenter() }
    private val mAdapter by lazy { activity?.let { FocusAdapter(it, itemList) }}
    private var loadingMore = false //是否加载更多

    init {
        mPresenter.attachView(this)
    }

    companion object {
        fun getInstance(title: String): FocusFragment{
            val fragment = FocusFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_focus

    override fun initView() {
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mAdapter
        //滑动到底部，自动加载更多
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItemPosition = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItemPosition == (itemCount - 1)){
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })
        //设置多布局
        mLayoutStatusView = multipleStatusView
    }

    /**
     * 初始化数据
     */
    override fun lazyLoad() {
        mPresenter.requestFocusInfo()
    }

    /**
     * 获取到数据之后的回调
     */
    override fun showFocusInfo(issue: HomeBean.Issue) {
        loadingMore = false
        itemList = issue.itemList
        mAdapter?.addData(issue.itemList)
    }

    override fun showErrorInfo(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            multipleStatusView.showNoNetwork()
        } else {
            multipleStatusView.showError()
        }
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