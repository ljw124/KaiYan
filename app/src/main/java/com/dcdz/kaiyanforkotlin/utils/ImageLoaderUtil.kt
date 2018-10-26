package com.dcdz.kaiyanforkotlin.utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget

object ImageLoaderUtil {

    /**
     * 常规使用
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView 目标view
     */
    fun LoadImage(context: Context, url: Any, imageView: ImageView) {
        Glide.with(context).load(url)
                .apply(RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions().crossFade(800))
                .into(imageView)
    }


    /**
     * 自定义RequestOptions使用
     *
     * @param context        上下文
     * @param url            图片链接
     * @param requestOptions
     * @param imageView      目标view
     */
    fun LoadImage(context: Context, url: Any, imageView: ImageView, requestOptions: RequestOptions) {
        Glide.with(context).load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions().crossFade(800))
                .into(imageView)
    }

    /**
     * 自定义RequestOptions使用
     *
     * @param fragment
     * @param url            图片链接
     * @param requestOptions
     * @param imageView      目标view
     */
    fun LoadImage(fragment: android.support.v4.app.Fragment, url: Any, imageView: ImageView, requestOptions: RequestOptions) {
        Glide.with(fragment).load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions().crossFade(800))
                .into(imageView)
    }

}
