package com.phat.testbase.view.ui.test

import androidx.fragment.app.FragmentManager
import com.phat.testbase.R
import com.phat.testbase.databinding.ActivityContainerBinding
import com.phat.testbase.devphat.xbase.BaseMainActivity
import com.phat.testbase.devphat.xbase.BaseMvvmFragment
import com.phat.testbase.devphat.xbase.BaseMvvmViewModel

class TestingActivity : BaseMainActivity<ActivityContainerBinding, BaseMvvmViewModel>(R.layout.activity_container) {

    override fun initFragment(): Array<BaseMvvmFragment<*, *>?> {
        val rootFragments: Array<BaseMvvmFragment<*, *>?> = arrayOfNulls(1)
        rootFragments[0] = TestingFragment()
        return rootFragments
    }

    override var containerLayoutId = R.id.containerFragment

    override fun setFragmentManager(): FragmentManager = supportFragmentManager
    override fun getVM(): Class<BaseMvvmViewModel> = BaseMvvmViewModel::class.java

}