package com.dcdz.kaiyanforkotlin.net

/**
 * Created by LJW on 2018/10/26.
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable){
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}