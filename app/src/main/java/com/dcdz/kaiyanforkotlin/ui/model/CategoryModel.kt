package com.dcdz.kaiyanforkotlin.ui.model

import com.dcdz.kaiyanforkotlin.bean.CategoryBean
import com.dcdz.kaiyanforkotlin.net.RetrofitManager
import com.dcdz.kaiyanforkotlin.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by LJW on 2018/10/31.
 * 分类
 */
class CategoryModel {

    /**
     * 获取分类数据
     */
    fun getCategoryData(): Observable<ArrayList<CategoryBean>> {

        return RetrofitManager.service.getCategory()
                .compose(SchedulerUtils.ioToMain())
    }
}