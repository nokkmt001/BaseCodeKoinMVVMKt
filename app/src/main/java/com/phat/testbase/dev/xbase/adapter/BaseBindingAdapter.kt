package com.phat.testbase.dev.xbase.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingAdapter<M, T : ViewDataBinding, VM : BaseBindingVH<T>> : RecyclerView.Adapter<VM>() {

    lateinit var binding: T

    var listAllData: MutableList<M>? = null

}
