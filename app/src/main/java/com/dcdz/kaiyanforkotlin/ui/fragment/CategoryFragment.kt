package com.dcdz.kaiyanforkotlin.ui.fragment

import android.os.Bundle
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment

/**
 * Created by LJW on 2018/10/30.
 * 分类
 */
class CategoryFragment: BaseFragment() {

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): CategoryFragment{
            val fragment = CategoryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_category

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}