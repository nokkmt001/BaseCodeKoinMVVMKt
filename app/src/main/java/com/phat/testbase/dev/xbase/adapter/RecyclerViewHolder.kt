package com.phat.testbase.dev.xbase.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewHolder<out T : Any>(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(
        position: Int,
        item: @UnsafeVariance T
    ) = Unit

    open fun bind(
        position: Int,
        item: @UnsafeVariance T,
        payloads: List<Any>
    ) = bind(position, item)
}