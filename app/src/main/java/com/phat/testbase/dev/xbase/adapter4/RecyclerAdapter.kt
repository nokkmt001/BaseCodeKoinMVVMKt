package com.phat.testbase.dev.xbase.adapter4

import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class RecyclerAdapter<T>(val view: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    open var items: MutableList<T>? = null
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    private val mOnEnabledListeners = hashSetOf<(Boolean) -> Unit>()

    open var isEnabled: Boolean = false
        set(value) {
            field = value
            mOnEnabledListeners.forEach { it(value) }
        }

    init {
        view.adapter = this
    }

    fun addOnEnableListener(function: (Boolean) -> Unit) {
        function(isEnabled)
        mOnEnabledListeners.add(function)
    }

    fun removeOnEnableListener(function: (Boolean) -> Unit) {
        mOnEnabledListeners.remove(function)
    }

    open fun submitList(items: MutableList<T>?) {
        this.items = items
    }

    open fun addItem(item: T) {
        if (items == null) {
            items = mutableListOf()
        }

        items?.add(item)
    }

    open fun addAllItem(listItem: MutableList<T>) {
        if (items == null) {
            items = mutableListOf()
        }

        items?.addAll(listItem)
//        notifyItemChanged(items.size)
    }


    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0 as? RecyclerHolder<T>)?.bind(getItem(p1))
    }

    protected open fun getItem(p1: Int): T {
        return items!![p1]
    }

    open fun getListItem(): MutableList<T>? {
        return items
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else (holder as? RecyclerHolder<T>)?.bind(getItem(position), payloads)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as? RecyclerHolder<T>)?.onRecycled()
    }

    override fun getItemCount() = if (items == null) 0 else items!!.size
}
