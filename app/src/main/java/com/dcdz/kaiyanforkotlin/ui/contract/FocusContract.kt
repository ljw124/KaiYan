package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean

/**
 * Created by LJW on 2018/10/31.
 * 关注
 */
interface FocusContract {

    interface View : IBaseView {
        /**
         * 显示关注信息
         */
        fun showFocusInfo(issue: HomeBean.Issue)

        /**
         * 显示错误信息
         */
        fun showErrorInfo(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取关注信息
         */
        fun requestFocusInfo()

        /**
         * 加载更多
         */
        fun loadMoreData()
    }
}