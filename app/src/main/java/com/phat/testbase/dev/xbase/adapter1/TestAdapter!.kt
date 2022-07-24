package com.phat.testbase.dev.xbase.adapter1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.phat.testbase.databinding.ItemPosterBinding
import com.phat.testbase.model.ResponBanner

class TestAdapter : BaseAdapter<ResponBanner>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ItemPosterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
//        (binding as ItemPosterBinding).viewModel?.item?.set(getItem(position))
//        binding.executePendingBindings()
    }

    override fun action(binding: ViewDataBinding, position: Int) {
        super.action(binding, position)
        (binding as ItemPosterBinding)

    }
}

val diffCallback = object : DiffUtil.ItemCallback<ResponBanner>() {
    override fun areContentsTheSame(oldItem: ResponBanner, newItem: ResponBanner): Boolean = false

    override fun areItemsTheSame(oldItem: ResponBanner, newItem: ResponBanner): Boolean = false
}