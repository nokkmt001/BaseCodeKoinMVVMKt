package com.phat.testbase.view.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phat.testbase.R
import com.phat.testbase.databinding.FragmentMainBinding
import com.phat.testbase.devphat.xbase.BaseMvvmFragment
import com.phat.testbase.devphat.xbase.BaseMvvmViewModel
import com.phat.testbase.view.ui.main.MainPagerAdapter
import org.koin.android.viewmodel.ext.android.getViewModel

class TestingFragment : BaseMvvmFragment<FragmentMainBinding, BaseMvvmViewModel>(R.layout.fragment_main) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return  binding {
            pagerAdapter = MainPagerAdapter(activity!!)
            vm = getViewModel()
            textTitle.text = "123456789"
        }.root
    }

}