package com.dcdz.kaiyanforkotlin.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.ErrorStatus
import com.dcdz.kaiyanforkotlin.ui.adapter.CategoryDetailAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.RankContract
import com.dcdz.kaiyanforkotlin.ui.presenter.RankPresenter
import com.dcdz.kaiyanforkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_rank.*

/**
 * Created by LJW on 2018/11/1.
 * 排行
 */
class RankFragment : BaseFragment(), RankContract.View{

    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { RankPresenter() }
    private val mAdapter by lazy { activity?.let { CategoryDetailAdapter(it, itemList, R.layout.item_category_detail) } }
    private var apiUrl: String? = null

    companion object {
        fun getInstance(url: String): RankFragment {
            val fragment = RankFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.apiUrl = url
            return fragment
        }
    }

    init {
        mPresenter.attachView(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_rank

    override fun initView() {
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mAdapter

        mLayoutStatusView = multipleStatusView
    }

    override fun lazyLoad() {
        if (!apiUrl.isNullOrEmpty()){
            mPresenter.loadRankList(apiUrl!!)
        }
    }

    override fun setRankList(itemList: ArrayList<HomeBean.Issue.Item>) {
        multipleStatusView.showContent()
        mAdapter?.addData(itemList)
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