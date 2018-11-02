package com.dcdz.kaiyanforkotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.dcdz.kaiyanforkotlin.utils.DisplayManager
import com.dcdz.kaiyanforkotlin.utils.log.CrashHandler
import com.dcdz.kaiyanforkotlin.utils.log.Log4jConfigure
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.crashreport.CrashReport
import kotlin.properties.Delegates

/**
 * Created by LJW on 2018/10/22.
 */
class MyApplication : Application() {

    protected var log = org.apache.log4j.Logger.getLogger(MyApplication::class.java!!)
    private var refWatcher: RefWatcher? = null

    companion object {
        private val TAG = "MyApplication"
        var context: Context by Delegates.notNull()
            private set //指定访问权限为私有

        fun getRefWatcher(context: Context): RefWatcher?{
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initConfig()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)

        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        val crashHandler = CrashHandler.getInstance()
        crashHandler.init(this)
        object : Thread() {
            override fun run() {
                Log4jConfigure.configure(filesDir.absolutePath)
                log.info("configure log4j ok")
            }
        }.start()

        //初始化腾讯Bugly
        //参数2：APPID，平台注册时得到; 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        CrashReport.initCrashReport(applicationContext, "1c706f9e26", false)
    }

    /**
     * 初始化配置
     */
    private fun initConfig() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("hao_zz")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }


    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }
}