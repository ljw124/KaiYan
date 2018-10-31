package com.dcdz.kaiyanforkotlin.ui.presenter

import com.dcdz.kaiyanforkotlin.base.BasePresenter
import com.dcdz.kaiyanforkotlin.net.ExceptionHandle
import com.dcdz.kaiyanforkotlin.ui.contract.CategoryContract
import com.dcdz.kaiyanforkotlin.ui.model.CategoryModel

/**
 * Created by LJW on 2018/10/31.
 * 分类
 */
class CategoryPresenter : BasePresenter<CategoryContract.View>(), CategoryContract.Presenter {

    private val categoryModel: CategoryModel by lazy {
        CategoryModel()
    }

    override fun loadCategoryData() {
        checkViewAttached()
        val disposable = categoryModel.getCategoryData()
                .subscribe({ categoryList ->
                    mRootView?.apply {
                        dismissLoading()
                        showCategoryInfo(categoryList)
                    }
                },{ t->
                    mRootView?.apply {
                        showErrorInfo(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                    }
                })
        addSubscription(disposable)
    }

}