package com.phat.testbase.devphat.xbase

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.phat.testbase.devphat.extensions.hideKeyboard

abstract class BaseMainActivity<T : ViewDataBinding, VM : BaseMvvmViewModel>(@LayoutRes private val contentLayoutId: Int)  : BaseMvvmActivity<T, VM>(contentLayoutId),
    FragmentHelper.FragmentAction {

    private var mFragmentHelper: FragmentHelper? = null

    private var mExitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentHelper = FragmentHelper(this)
    }

    override fun onBackPressed() {
        hideKeyboard()

        if (mFragmentHelper!!.currentPageSize > 1) {
            popFragment()
            return
        }

        if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
            finish()
        } else {
            mExitTime = System.currentTimeMillis()
        }
    }
    override fun pushFragment(baseFragment: BaseMvvmFragment<*, *>) {
        super.pushFragment(baseFragment)
        mFragmentHelper!!.pushFragment(baseFragment)
    }

    override fun popFragment() {
        super.popFragment()
        mFragmentHelper!!.popFragment()
    }

    override fun popToFragment(baseFragment: BaseMvvmFragment<*, *>) {
        super.popToFragment(baseFragment)
        mFragmentHelper!!.popToFragment(baseFragment::class.java)
    }

    override fun popToRootFragment() {
        super.popToRootFragment()
        mFragmentHelper!!.popFragmentToRoot()
    }

    fun getFragmentHelper() = mFragmentHelper

}
