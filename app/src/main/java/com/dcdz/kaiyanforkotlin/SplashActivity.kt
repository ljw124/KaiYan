package com.dcdz.kaiyanforkotlin

import android.Manifest
import android.content.Intent
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.dcdz.kaiyanforkotlin.base.BaseActivity
import com.dcdz.kaiyanforkotlin.utils.AppUtils
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.EasyPermissions

class SplashActivity : BaseActivity() {

    //渐变动画
    private var alphaAnimation : AlphaAnimation? = null

    override fun layoutId(): Int = R.layout.activity_splash

    override fun initView() {
        alphaAnimation = AlphaAnimation(0.2f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                toMain()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })

        //动画在检查权限之后启动
        checkPermission()
    }

    override fun initData() {
        //设置版本号
        tv_version_name.text = "当前版本 V：" + AppUtils.getVerName(this)
    }

    override fun start() {
    }

    fun toMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkPermission(){
        val perms = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        EasyPermissions.requestPermissions(this, "应用需要以下权限，请允许", 0, *perms) //* 用于将数组传递给 vararg 参数
    }

    //检查权限之后回调此方法
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == 0){
            if (perms.contains(Manifest.permission.READ_PHONE_STATE) && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if (alphaAnimation != null){
                    iv_web_icon.startAnimation(alphaAnimation)
                }
            }
        }
    }
}
