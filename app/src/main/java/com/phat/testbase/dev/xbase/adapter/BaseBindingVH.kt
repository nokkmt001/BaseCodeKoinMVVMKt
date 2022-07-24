package com.phat.testbase.dev.xbase.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Furkan on 2019-10-16
 */

abstract class BaseBindingVH<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T)

    open fun listener(item: T) {}
}
