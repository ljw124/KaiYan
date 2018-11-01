package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.bean.TabInfoBean

/**
 * Created by LJW on 2018/11/1.
 * 排行
 */
interface RankContract {

    interface View : IBaseView {
        /**
         * 设置排行榜的数据
         */
        fun setRankList(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * 显示错误信息
         */
        fun showErrorInfo(errorMsg:String,errorCode:Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取 Tab 数据
         */
        fun loadRankList(apiUrl: String)
    }
}