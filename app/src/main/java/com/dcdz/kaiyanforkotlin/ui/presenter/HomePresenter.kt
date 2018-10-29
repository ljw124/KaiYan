package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.ui.contract.HomeContract

/**
 * Created by LJW on 2018/10/29.
 */
class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun loadHomeData(num: Int) {

    }

    override fun loadMoreData() {
    }
}