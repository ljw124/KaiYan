package com.dcdz.kaiyanforkotlin.ui.fragment


import android.os.Bundle
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.ui.contract.HomeContract

class HomeFragment : BaseFragment(), HomeContract.View {


    companion object {
        fun getInstance(title: String): HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
    }

    override fun lazyLoad() {
    }

    override fun setHomeData(homeBean: HomeBean) {
    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    private var mTitle: String? = null

}
