package com.dcdz.kaiyanforkotlin.bean

import java.io.Serializable

/**
 * Created by LJW on 2018/10/22.
 * desc:分类 Bean
 */
data class CategoryBean(val id: Long, val name: String, val description: String, val bgPicture: String, val bgColor: String, val headerImage: String) : Serializable
