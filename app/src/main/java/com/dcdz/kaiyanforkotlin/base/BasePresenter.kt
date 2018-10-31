package com.dcdz.kaiyanforkotlin.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.apache.log4j.Logger

/**
 * Created by LJW on 2018/10/22.
 */
open class BasePresenter<T: IBaseView> : IPresenter<T> {

    internal var log = Logger.getLogger(BasePresenter::class.java!!)

    var mRootView: T? = null
        private set

    private var compositeDisposable = CompositeDisposable()

    override fun attachView(mRootView: T) {
        this.mRootView = mRootView
    }

    override fun detachView() {
        mRootView = null
        //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed){
            compositeDisposable.clear()
        }
    }

    fun addSubscription(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    private val isViewAttached: Boolean
        get() = mRootView != null

    fun checkViewAttached(){
        if (!isViewAttached){
            throw MvpViewNotAttachedException()
        }
    }

    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")
}