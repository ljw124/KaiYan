package com.dcdz.kaiyanforkotlin.ui.model

import com.dcdz.kaiyanforkotlin.bean.TabInfoBean
import com.dcdz.kaiyanforkotlin.net.RetrofitManager
import com.dcdz.kaiyanforkotlin.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by LJW on 2018/11/1.
 * 热门 - tab
 */
class HotTabModel {

    /**
     * 获取 Tab 信息
     */
    fun loadTabInfo() : Observable<TabInfoBean> {

        return RetrofitManager.service.getRankList()
                .compose(SchedulerUtils.ioToMain())
    }
}