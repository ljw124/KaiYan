package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.net.ExceptionHandle
import com.dcdz.kaiyanforkotlin.ui.contract.RankContract
import com.dcdz.kaiyanforkotlin.ui.model.RankModel

/**
 * Created by LJW on 2018/11/1.
 * 排行
 */
class RankPresenter : BasePresenter<RankContract.View>(), RankContract.Presenter{

    private val rankModel by lazy { RankModel() }

    override fun loadRankList(apiUrl: String) {
        checkViewAttached()
        val disposable = rankModel.loadRankList(apiUrl)
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        setRankList(issue.itemList)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        showErrorInfo(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                        log.error("RankPresenter -> " + throwable.message)
                    }
                })
        addSubscription(disposable)
    }
}