package com.dcdz.kaiyanforkotlin.ui.contract

import com.dcdz.kaiyanforkotlin.base.IBaseView
import com.dcdz.kaiyanforkotlin.base.IPresenter
import com.dcdz.kaiyanforkotlin.bean.HomeBean

/**
 * Created by LJW on 2018/11/1.
 * 搜索
 */
interface SearchContract {

    interface View : IBaseView {
        /**
         * 设置热门关键词
         */
        fun setHotWord(hotWord: ArrayList<String>)

        /**
         * 设置搜索的结果
         */
        fun setSearchResult(issue: HomeBean.Issue)

        /**
         * 关闭软键盘
         */
        fun closeSoftKeyboard()

        /**
         * 设置空 View
         */
        fun setEmptyView()

        /**
         * 显示错误信息
         */
        fun showErrorInfo(errorMsg: String,errorCode:Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 加载热门关键词数据
         */
        fun loadHotWordData()

        /**
         * 查询搜索
         */
        fun loadSearchData(words: String)

        /**
         * 加载更多
         */
        fun loadMoreData()
    }
}