package com.dcdz.kaiyanforkotlin.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dcdz.kaiyanforkotlin.R
import com.dcdz.kaiyanforkotlin.base.BaseFragment
import com.dcdz.kaiyanforkotlin.bean.CategoryBean
import com.dcdz.kaiyanforkotlin.net.ErrorStatus
import com.dcdz.kaiyanforkotlin.ui.adapter.CategoryAdapter
import com.dcdz.kaiyanforkotlin.ui.contract.CategoryContract
import com.dcdz.kaiyanforkotlin.ui.presenter.CategoryPresenter
import com.dcdz.kaiyanforkotlin.utils.DisplayManager
import com.dcdz.kaiyanforkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_category.*

/**
 * Created by LJW on 2018/10/30.
 * 分类
 */
class CategoryFragment: BaseFragment(), CategoryContract.View {

    private var mTitle: String? = null
    private var categoryList = ArrayList<CategoryBean>()
    private val mPresenter by lazy { CategoryPresenter() }
    private val mAdapter by lazy { activity?.let { CategoryAdapter(it, categoryList, R.layout.item_category) } }

    companion object {
        fun getInstance(title: String): CategoryFragment{
            val fragment = CategoryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_category

    @Suppress("DEPRECATION")
    override fun initView() {
        //绑定View
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView

        mRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        mRecyclerView.adapter = mAdapter
        //添加自定义分割线
        mRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                val position = parent.getChildPosition(view)
                val offset = DisplayManager.dip2px(4f)!!
                //设置左上右下的间隔
                outRect.set(if (position % 2 ==0) 0 else offset, offset,
                        if (position % 2 == 0) offset else 0, offset)
            }
        })
    }

    override fun lazyLoad() {
        mPresenter.loadCategoryData()
    }

    /**
     * 获取到分类数据后的回调
     */
    override fun showCategoryInfo(categoryList: ArrayList<CategoryBean>) {
        this.categoryList = categoryList
        mAdapter?.setData(categoryList)
    }

    override fun showErrorInfo(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            multipleStatusView?.showNoNetwork()
        } else {
            multipleStatusView?.showError()
        }
    }

    override fun showLoading() {
        multipleStatusView?.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView?.showContent()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}