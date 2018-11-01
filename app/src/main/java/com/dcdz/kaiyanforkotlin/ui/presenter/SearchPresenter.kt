package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.net.ExceptionHandle
import com.dcdz.kaiyanforkotlin.ui.contract.SearchContract
import com.dcdz.kaiyanforkotlin.ui.model.SearchModel

/**
 * Created by LJW on 2018/11/1.
 * 搜索
 */
class SearchPresenter: BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private var nextPageUrl: String? = null
    private val searchModel by lazy { SearchModel() }

    /**
     * 获取热门关键词
     */
    override fun loadHotWordData() {
        checkViewAttached()
        mRootView?.apply {
            closeSoftKeyboard()
            showLoading()
        }
        addSubscription(disposable = searchModel.loadHotWordData()
                .subscribe({ string ->
                    mRootView?.apply {
                        setHotWord(string)
                    }
                }, {throwable ->
                    mRootView?.apply {
                        showErrorInfo(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                    }
                })
        )
    }

    /**
     * 通过关键词查询的结果
     */
    override fun loadSearchData(words: String) {
        checkViewAttached()
        mRootView?.apply {
            closeSoftKeyboard()
            showLoading()
        }
        addSubscription(disposable = searchModel.loadSearchResult(words)
                .subscribe({issue ->
                    mRootView?.apply {
                        dismissLoading()
                        if (issue.count > 0 && issue.itemList.size> 0){
                            nextPageUrl = issue.nextPageUrl
                            setSearchResult(issue)
                        } else {
                            setEmptyView()
                        }
                    }
                }, { throwable ->
                    mRootView?.apply {
                        dismissLoading()
                        showErrorInfo(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                    }
                })
        )
    }

    /**
     * 加载更多数据
     */
    override fun loadMoreData() {
        checkViewAttached()
        nextPageUrl?.let {
            addSubscription(disposable = searchModel.loadMoreData(it)
                    .subscribe({ issue ->
                        mRootView?.apply {
                            nextPageUrl = issue.nextPageUrl
                            setSearchResult(issue)
                        }
                    }, { throwable ->
                        mRootView?.apply {
                            showErrorInfo(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                        }
                    })
            )
        }
    }
}