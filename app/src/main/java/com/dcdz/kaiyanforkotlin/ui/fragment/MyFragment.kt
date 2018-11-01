package com.dcdz.kaiyanforkotlin.ui.fragment

import android.os.Bundle
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment

/**
 * Created by LJW on 2018/11/1.
 */
class MyFragment : BaseFragment() {

    private var mTitle:String? =null

    companion object {
        fun getInstance(title:String): MyFragment {
            val fragment = MyFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_my

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}