package com.dcdz.kaiyanforkotlin.ui.model

import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.net.RetrofitManager
import com.dcdz.kaiyanforkotlin.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by LJW on 2018/11/1.
 * 排行
 */
class RankModel {

    /**
     * 获取排行榜数据
     */
    fun loadRankList(apiUrl: String) : Observable<HomeBean.Issue> {

        return RetrofitManager.service.getIssueData(apiUrl)
                .compose(SchedulerUtils.ioToMain())
    }
}