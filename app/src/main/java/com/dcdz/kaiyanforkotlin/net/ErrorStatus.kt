package com.dcdz.kaiyanforkotlin.net

/**
 * Created by LJW on 2018/10/26.
 * object 就是单例模式的化身
 */
object ErrorStatus {

    /**
     * 未知错误
     */
    @JvmField //相当于把类的属性暴露出去
    val UNKNOWN_ERROR = 1002

    /**
     * 数据解析错误
     */
    @JvmField
    val PARSE_ERROR = 1003

    /**
     * 网络连接超时
     */
    @JvmField
    val NETWORK_ERROR = 1004

    /**
     * API解析异常（或者第三方数据结构更改）等其他异常
     */
    @JvmField
    val API_ERROR = 1005

}