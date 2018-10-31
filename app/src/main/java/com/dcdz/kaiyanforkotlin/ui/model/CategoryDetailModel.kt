package com.dcdz.kaiyanforkotlin.ui.model

import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.RetrofitManager
import com.dcdz.kaiyanforkotlin.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by LJW on 2018/10/31.
 * 分类详情
 */
class CategoryDetailModel {

    /**
     * 获取详情数据
     */
    fun getCategoryDetailList(id: Long): Observable<HomeBean.Issue> {

        return RetrofitManager.service.getCategoryDetailList(id)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData(url: String): Observable<HomeBean.Issue> {

        return RetrofitManager.service.getIssueData(url)
                .compose(SchedulerUtils.ioToMain())
    }
}