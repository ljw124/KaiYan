package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.CategoryBean

/**
 * Created by LJW on 2018/10/31.
 * 分类
 */
interface CategoryContract {

    interface View : IBaseView {
        /**
         * 显示分类的信息
         */
        fun showCategoryInfo(categoryList: ArrayList<CategoryBean>)

        /**
         * 显示错误信息
         */
        fun showErrorInfo(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View>{
        /**
         * 获取分类的信息
         */
        fun loadCategoryData()
    }
}