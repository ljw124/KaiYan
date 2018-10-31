package com.dcdz.kaiyanforkotlin.ui.model

import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.RetrofitManager
import com.dcdz.kaiyanforkotlin.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by LJW on 2018/10/31.
 */
class FocusModel {

    /**
     * 获取关注信息
     */
    fun requestFocusInfo(): Observable<HomeBean.Issue> {

        return RetrofitManager.service.getFocusInfo()
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载跟多数据
     */
    fun loadMoreData(url: String): Observable<HomeBean.Issue> {

        return RetrofitManager.service.getIssueData(url)
                .compose(SchedulerUtils.ioToMain())
    }
}