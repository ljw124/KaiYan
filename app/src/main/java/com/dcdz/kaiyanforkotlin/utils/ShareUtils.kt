package com.dcdz.kaiyanforkotlin.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

/**
 * 一键分享
 */
object ShareUtils {

    /**
     * 文字分享
     *
     * @param context 上下文
     * @param content 内容
     */
    fun shareText(context: Context, content: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.type = "text/plain"
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享到"))
    }

    /**
     * 分享单张图片
     */
    fun shareSingleImage(context: Context, imageUrl: String) {
        //由文件得到uri
        val imageUri = Uri.parse(imageUrl)
        Log.d("share", "uri:$imageUri")
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.type = "image/*"
        context.startActivity(Intent.createChooser(shareIntent, "分享到"))
    }
}
