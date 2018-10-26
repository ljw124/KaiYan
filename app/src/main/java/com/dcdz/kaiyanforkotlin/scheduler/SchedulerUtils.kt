package com.dcdz.kaiyanforkotlin.scheduler

/**
 * Created by LJW on 2018/10/26.
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }
}