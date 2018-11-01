package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.net.ExceptionHandle
import com.dcdz.kaiyanforkotlin.ui.contract.HotTabContract
import com.dcdz.kaiyanforkotlin.ui.model.HotTabModel

/**
 * Created by LJW on 2018/11/1.
 * 热门 - tab
 */
class HotTabPresenter : BasePresenter<HotTabContract.View>(), HotTabContract.Presenter{

    private val hotTabModel by lazy { HotTabModel() }

    override fun loadTabInfo() {
        checkViewAttached()
        val disposable = hotTabModel.loadTabInfo()
                .subscribe({ tabInfo ->
                    mRootView?.setTabInfo(tabInfo)
                }, { t->
                    mRootView?.showErrorInfo(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                    log.error("HotTabPresenter -> " + t.message)
                })
        addSubscription(disposable)
    }
}