package com.dcdz.kaiyanforkotlin.bean

/**
 * Created by LJW on 2018/10/22.
 * desc: 热门的 tabInfo
 */
data class TabInfoBean(val tabInfo: TabInfo) {
    data class TabInfo(val tabList: ArrayList<Tab>)

    data class Tab(val id: Long, val name: String, val apiUrl: String)
}
