package com.dcdz.kaiyanforkotlin.net

import com.google.gson.JsonParseException
import org.apache.log4j.Logger
import org.json.JSONException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Created by LJW on 2018/10/26.
 * 此类用来封装错误返回信息
 */
class ExceptionHandle {


    //companion object 就是 Java 中的 static 变量
    companion object {
        //初始化一个错误码
        var errorCode = ErrorStatus.UNKNOWN_ERROR
        var errorMsg = "请求失败，请稍后重试"
        var logger = Logger.getLogger(ExceptionHandle::class.java)

        fun handleException(e: Throwable): String {
            e.printStackTrace()
            if (e is SocketTimeoutException){
                logger.error("网络连接异常:" + e.message)
                errorMsg = "网络连接异常"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else if (e is JsonParseException || e is JSONException || e is ParseException){
                logger.error("数据解析异常:" + e.message)
                errorMsg = "数据解析异常"
                errorCode = ErrorStatus.PARSE_ERROR
            } else if (e is ApiException){
                logger.error("服务器返回数据异常:" + e.message)
                errorMsg = "服务器返回数据异常"
                errorCode = ErrorStatus.PARSE_ERROR
            } else if (e is IllegalArgumentException) {
                logger.error("参数错误:" + e.message)
                errorMsg = "参数错误"
                errorCode = ErrorStatus.PARSE_ERROR
            } else if (e is UnknownHostException) {
                logger.error("网络连接异常: " + e.message)
                errorMsg = "网络连接异常"
                errorCode = ErrorStatus.NETWORK_ERROR
            } else { //未知错误
                logger.error("未知错误:" + e.message)
                errorMsg = "未知错误"
                errorCode = ErrorStatus.UNKNOWN_ERROR
            }
            return errorMsg
        }

    }
}