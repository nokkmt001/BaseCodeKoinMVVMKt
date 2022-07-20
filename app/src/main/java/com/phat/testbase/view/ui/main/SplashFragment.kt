package com.phat.testbase.view.ui.main

import com.phat.testbase.R
import com.phat.testbase.databinding.FragmentMainBinding
import com.phat.testbase.dev.xbase.BaseMvvmFragment
import com.phat.testbase.dev.xbase.EmptyViewModel

class SplashFragment : BaseMvvmFragment<FragmentMainBinding, EmptyViewModel>(R.layout.fragment_main) {

    override fun getVM(): Class<EmptyViewModel> = EmptyViewModel::class.java


}