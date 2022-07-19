package com.phat.testbase.view.ui.test

import com.phat.testbase.R
import com.phat.testbase.databinding.FragmentMainBinding
import com.phat.testbase.dev.xbase.BaseMvvmFragment
import com.phat.testbase.dev.xbase.BaseMvvmViewModel

class TestingFragment : BaseMvvmFragment<FragmentMainBinding, BaseMvvmViewModel>(R.layout.fragment_main) {

    override fun startFlow() {
        super.startFlow()
        binding.run {
            textTitle.text = "123456789"
        }
    }

}