package com.dcdz.kaiyanforkotlin.api

import com.dcdz.kaiyanforkotlin.bean.CategoryBean
import com.dcdz.kaiyanforkotlin.bean.HomeBean
import com.dcdz.kaiyanforkotlin.bean.TabInfoBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by LJW on 2018/10/22.
 */
interface ApiService {

    /**
     * 首页精选
     */
    @GET("v2/feed?")
    fun getFirstHomeData(@Query("nu")num: Int): Observable<HomeBean>

    /**
     * 根据nextPageUrl 请求下一页数据
     */
    @GET
    fun getMoreHomeData(@Url url: String): Observable<HomeBean>

    /**
     * 根据item id 获取相关视频
     */
    @GET("v4/video/related?")
    fun getRelatedData(@Query("id")id: Long): Observable<HomeBean.Issue>

    /**
     * 获取分类
     */
    @GET("v4/categories")
    fun getCategory(): Observable<ArrayList<CategoryBean>>

    /**
     * 获取分类详情list
     */
    @GET("v4/categories/videoList?")
    fun getCategoryList(@Query("id")id: Long): Observable<HomeBean.Issue>

    /**
     * 获取更多 Issue
     */
    @GET
    fun getIssueData(@Url url: String): Observable<HomeBean.Issue>

    /**
     * 获取全部排行榜的Info（包括，title 和 Url）
     */
    @GET("v4/rankList")
    fun getRankList():Observable<TabInfoBean>

    /**
     * 获取搜索信息
     */
    @GET("v1/search?&num=10&start=10")
    fun getSearchData(@Query("query") query :String) : Observable<HomeBean.Issue>

    /**
     * 热门搜索词
     */
    @GET("v3/queries/hot")
    fun getHotWord():Observable<ArrayList<String>>

    /**
     * 关注
     */
    @GET("v4/tabs/follow")
    fun getFollowInfo():Observable<HomeBean.Issue>

    /**
    //首页精选
    http://baobab.kaiyanapp.com/api/v2/feed?

    //相关视频
    http://baobab.kaiyanapp.com/api/v4/video/related?id=xxx

    //获取分类
    http://baobab.kaiyanapp.com/api/v4/categories

    //获取分类详情List
    http://baobab.kaiyanapp.com/api/v4/categories/videoList?id=xxx&udid=xxx

    //获取排行榜的 Info
    http://baobab.kaiyanapp.com/api/v4/rankList

    //获取搜索信息
    http://baobab.kaiyanapp.com/api/v1/search?&num=10&start=10&query=xxx

    //热门搜索关键词
    http://baobab.kaiyanapp.com/api/v3/queries/hot

    //关注
    http://baobab.kaiyanapp.com/api/v4/tabs/follow

    //热门搜索词
    http://baobab.kaiyanapp.com/api/v3/queries/hot
    ["阅后即瞎","日食记","复仇者联盟","励志","谷阿莫","复仇者联盟3","美食","广告","爱情","舞蹈","搞笑","漫威","动画","日本","电影相关","健身","VR","滑板","脱口秀","寻梦环游记"]

     */
}