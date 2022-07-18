package com.phat.testbase.view.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.skydoves.bindables.BindingFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class BaseMvvmFragment<T : ViewDataBinding, VM : BaseMvvmViewModel>(@LayoutRes private val contentLayoutId: Int) : BindingFragment<T>(contentLayoutId)  {

    lateinit var viewModel : VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel =

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFlow()
        initData()
        initVM()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()


    }

    override fun onDetach() {
        super.onDetach()
    }

    fun startFlow(){}

    fun initData(){}

    fun initVM(){}
}