package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.TabInfoBean

/**
 * Created by LJW on 2018/11/1.
 * 热门 - tab
 */
interface HotTabContract {

    interface View : IBaseView {
        /**
         * 设置 Tab 信息
         */
        fun setTabInfo(tabInfo: TabInfoBean)
        /**
         * 显示错误信息
         */
        fun showErrorInfo(errorMsg:String,errorCode:Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取 Tab 信息
         */
        fun loadTabInfo()
    }
}