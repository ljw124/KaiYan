package com.dcdz.kaiyanforkotlin.base

import com.dcdz.kaiyanforkotlin.base.IBaseView

/**
 * Created by LJW on 2018/10/22.
 */
interface IPresenter<in V: IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()
}