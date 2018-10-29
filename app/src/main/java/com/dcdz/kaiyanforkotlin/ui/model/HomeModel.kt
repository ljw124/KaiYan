package com.dcdz.kaiyanforkotlin.ui.model

import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.RetrofitManager
import com.dcdz.kaiyanforkotlin.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by LJW on 2018/10/29.
 */
class HomeModel {

    /**
     * 获取首页 Banner 数据
     */
    fun loadHomeData(num: Int): Observable<HomeBean>{
        return RetrofitManager.service.getFirstHomeData(num)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData(url: String): Observable<HomeBean>{
        return RetrofitManager.service.getMoreHomeData(url)
                .compose(SchedulerUtils.ioToMain())
    }
}