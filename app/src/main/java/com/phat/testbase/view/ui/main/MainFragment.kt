package com.phat.testbase.view.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phat.testbase.R
import com.phat.testbase.databinding.FragmentMainBinding
import com.skydoves.bindables.BindingFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textTitle.text = "123456789"
    }
}