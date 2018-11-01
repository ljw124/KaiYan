package com.dcdz.kaiyanforkotlin.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my.*

/**
 * Created by LJW on 2018/11/1.
 */
class MyFragment : BaseFragment() {

    private var mTitle:String? =null

    companion object {
        fun getInstance(title:String): MyFragment {
            val fragment = MyFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_my

    override fun initView() {
        tvBlobUrl.setOnClickListener{
            val uri = Uri.parse("https://blog.csdn.net/ljw124213")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity!!.startActivity(intent)
        }
        tvGithubUrl.setOnClickListener {
            val uri = Uri.parse("https://github.com/ljw124/KaiYan")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity!!.startActivity(intent)
        }
        tvEmailUrl.setOnClickListener {
            val uri = Uri.parse("https://github.com/ljw124/KaiYan")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity!!.startActivity(intent)
        }
    }

    override fun lazyLoad() {
    }
}