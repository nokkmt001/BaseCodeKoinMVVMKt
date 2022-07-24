package com.techup.pos.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phat.testbase.dev.xbase.adapter3.BaseRecyclerViewViewHolder
import com.techup.pos.R

class LoadMoreViewHolder<T>(view: View) : BaseRecyclerViewViewHolder<T>(view) {

    override fun bind(item: T) {}

    companion object {
        fun <M>create(parent: ViewGroup): LoadMoreViewHolder<M> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_load_more, parent, false)
            return LoadMoreViewHolder(itemView)
        }
    }

}
