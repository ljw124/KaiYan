package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.ui.contract.CategoryDetailContract
import com.dcdz.kaiyanforkotlin.ui.model.CategoryDetailModel

/**
 * Created by LJW on 2018/10/31.
 * 分类详情
 */
class CategoryDetailPresenter : BasePresenter<CategoryDetailContract.View>(), CategoryDetailContract.Presenter {

    private val categoryDetailModel by lazy {
        CategoryDetailModel()
    }

    private var nextPageUrl: String? = null

    /**
     * 获取分类详情的列表信息
     */
    override fun loadCategoryDetailList(id: Long) {
        checkViewAttached()
        val disposable= categoryDetailModel.getCategoryDetailList(id)
                .subscribe({
                    issue ->
                    mRootView?.apply {
                        nextPageUrl = issue.nextPageUrl
                        showCategoryDetailData(issue.itemList)
                    }
                },{ t ->
                    mRootView?.apply {
                        showErrorInfo(t.toString())
                        log.error("CategoryDetailPresenter-getCategoryDetailList -> " + t.toString())
                    }
                })
        addSubscription(disposable)
    }

    /**
     * 加载更多数据
     */
    override fun loadMoreData() {
        val disposable = nextPageUrl?.let {
            categoryDetailModel.loadMoreData(it)
                    .subscribe({ issue ->
                        mRootView?.apply {
                            nextPageUrl = issue.nextPageUrl
                            showCategoryDetailData(issue.itemList)
                        }
                    }, { t ->
                        mRootView?.apply {
                            showErrorInfo(t.toString())
                            log.error("CategoryDetailPresenter-loadMoreData -> " + t.toString())
                        }
                    })
        }
        disposable?.let { addSubscription(it) }
    }
}