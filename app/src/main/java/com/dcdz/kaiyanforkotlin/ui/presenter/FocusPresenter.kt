package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.net.ExceptionHandle
import com.dcdz.kaiyanforkotlin.ui.contract.FocusContract
import com.dcdz.kaiyanforkotlin.ui.model.FocusModel

/**
 * Created by LJW on 2018/10/31.
 * 关注
 */
class FocusPresenter : BasePresenter<FocusContract.View>(), FocusContract.Presenter {

    private val focusModel by lazy { FocusModel() }

    private var nextPageUrl: String? = null

    /**
     *  请求关注数据
     */
    override fun requestFocusInfo() {
        checkViewAttached()
        val disposable = focusModel.requestFocusInfo()
                .subscribe({issue ->
                    mRootView?.apply {
                        dismissLoading()
                        nextPageUrl = issue.nextPageUrl
                        showFocusInfo(issue)
                    }
                }, {throwable ->
                    mRootView?.apply {
                        showErrorInfo(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                })
        addSubscription(disposable)
    }

    /**
     * 加载更多
     */
    override fun loadMoreData() {
        val disposable = nextPageUrl?.let {
            focusModel.loadMoreData(it)
                    .subscribe({ issue ->
                        mRootView?.apply {
                            nextPageUrl = issue.nextPageUrl
                            showFocusInfo(issue)
                        }
                    }, { t ->
                        mRootView?.apply {
                            showErrorInfo(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                        }
                    })
        }
        if (disposable != null){
            addSubscription(disposable)
        }
    }
}