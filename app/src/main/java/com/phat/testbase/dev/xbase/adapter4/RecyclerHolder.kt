package com.phat.testbase.dev.xbase.adapter4

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

@Suppress("UNCHECKED_CAST")
open class RecyclerHolder<T>(private val parent: ViewGroup,
                             @LayoutRes id: Int) : RecyclerView.ViewHolder(inflate(parent, id)), LayoutContainer {
    override val containerView = itemView

    var item: T? = null
        private set

    private val mAdapter get() = ((parent as RecyclerView).adapter as RecyclerAdapter<*>)

    open fun bind(item: T) {
        this.item = item
    }

    open fun bind(item: T, payload: Any?) {
        this.item = item
    }

    open fun onRecycled() {
    }

    fun fitSpanCount(count: Int, byWidth: Boolean) {
        itemView.layoutParams.apply {
            width = parent.measuredWidth / count
            height = width
        }
    }

    val isAtFirst get() = adapterPosition == 0

    open val isAtLast: Boolean
        get() {
            return adapterPosition == mAdapter.itemCount - 1
        }

    companion object {
        fun inflate(parent: ViewGroup, id: Int): View =
            LayoutInflater.from(parent.context)
                .inflate(id, parent, false)
    }

}
