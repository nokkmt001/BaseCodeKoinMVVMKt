package com.phat.testbase.view.ui.main

import android.graphics.Color
import android.view.View
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.phat.testbase.R
import com.phat.testbase.databinding.FragmentHomeBinding
import com.phat.testbase.dev.extensions.launchWhenCreated
import com.phat.testbase.dev.view.TestViewModel
import com.phat.testbase.dev.xbase.BaseMvvmFragment

class TestFragment : BaseMvvmFragment<FragmentHomeBinding, TestViewModel>(R.layout.fragment_home) {

    override fun startFlow() {
        super.startFlow()
        viewModel.testDataBanner()
        launchWhenCreated {
            viewModel.resultBanner.collect{

            }
        }
    }
    private fun getTransform(mStartView: View, mEndView: View): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            startView = mStartView
            endView = mEndView
            addTarget(mEndView)
            pathMotion = MaterialArcMotion()
            duration = 550
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun getVM(): Class<TestViewModel> = TestViewModel::class.java
}