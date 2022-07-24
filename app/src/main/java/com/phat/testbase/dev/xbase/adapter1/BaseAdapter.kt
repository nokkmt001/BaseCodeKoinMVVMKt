package com.phat.testbase.dev.xbase.adapter1

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.phat.testbase.R

/**
 * Created by Furkan on 2019-10-16
 */

abstract class BaseAdapter<T>(callback: DiffUtil.ItemCallback<T>) : ListAdapter<T, BaseViewHolder<ViewDataBinding>>(
    callback
) {

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        bind(holder.binding, position)
        action(holder.binding, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = getViewHolder(
        parent,
        viewType
    )

    open fun getViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder(
        createBinding(parent, viewType)
    )

    abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    protected abstract fun bind(binding: ViewDataBinding, position: Int)

    protected open fun action(binding: ViewDataBinding, position: Int) {}
}
