package com.dcdz.kaiyanforkotlin.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import com.dcdz.kaiyanforkotlin.base.BaseFragmentAdapter
import com.dcdz.kaiyanforkotlin.bean.TabInfoBean
import com.dcdz.kaiyanforkotlin.net.ErrorStatus
import com.dcdz.kaiyanforkotlin.ui.contract.HotTabContract
import com.dcdz.kaiyanforkotlin.ui.presenter.HotTabPresenter
import com.dcdz.kaiyanforkotlin.utils.StatusBarUtil
import com.dcdz.kaiyanforkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by LJW on 2018/11/1.
 * 热门
 */
class HotFragment : BaseFragment(), HotTabContract.View {

    private var mTitle: String? = null
    private val mPresenter by lazy { HotTabPresenter() }
    private val mTabTitles = ArrayList<String>() //tab 标题
    private val mFragments = ArrayList<Fragment>()

    companion object {
        fun getInstance(title: String): HotFragment {
            val fragment = HotFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    init {
        mPresenter.attachView(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_hot

    override fun initView() {
        mLayoutStatusView = multipleStatusView

        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }
    }

    override fun lazyLoad() {
        mPresenter.loadTabInfo()
    }

    /**
     * 获取到 Tab 数据之后回调
     */
    override fun setTabInfo(tabInfo: TabInfoBean) {
        multipleStatusView.showContent()

        //把tab的name映射到集合中
        tabInfo.tabInfo.tabList.mapTo(mTabTitles) {
            it.name
        }
        //根据url来区分不同fragment
        tabInfo.tabInfo.tabList.mapTo(mFragments){
            RankFragment.getInstance(it.apiUrl)
        }

        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager, mFragments, mTabTitles)
        mTabLayout.setupWithViewPager(mViewPager)
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