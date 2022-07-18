package com.phat.testbase.devphat.view

import com.phat.testbase.R
import com.phat.testbase.databinding.ActivityMainBinding
import com.phat.testbase.view.ui.base.BaseMvvmActivity

class TestActivity : BaseMvvmActivity<ActivityMainBinding, TestViewModel>(R.layout.activity_main) {

    override fun startFlow() {
        super.startFlow()


    }

    override fun initVM() {
        super.initVM()
        viewModel.eventLoading.
    }
}