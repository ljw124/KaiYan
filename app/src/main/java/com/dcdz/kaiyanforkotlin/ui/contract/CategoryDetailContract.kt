package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean

/**
 * Created by LJW on 2018/10/31.
 * 分类详情
 */
interface CategoryDetailContract {

    interface View : IBaseView {
        /**
         * 显示详情数据
         */
        fun showCategoryDetailData(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * 显示错误信息
         */
        fun showErrorInfo(errorMsg: String)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取详情数据
         */
        fun loadCategoryDetailList(id: Long)

        /**
         * 加载更多数据
         */
        fun loadMoreData()
    }
}