package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean

/**
 * Created by LJW on 2018/10/29.
 */
interface HomeContract {

    interface View : IBaseView{
        /**
         * 第一次请求数据
         */
        fun setHomeData(homeBean: HomeBean)

        /**
         * 请求获取更多数据
         */
        fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View>{

        /**
         * 加载首页数据
         */
        fun loadHomeData(num: Int)

        /**
         * 加载更多数据
         */
        fun loadMoreData()
    }
}