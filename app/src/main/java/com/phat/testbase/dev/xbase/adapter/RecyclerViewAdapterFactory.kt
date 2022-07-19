package com.phat.testbase.dev.xbase.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class RecyclerViewAdapterFactory<T : Any> :
    AdapterFactory<T, ListAdapter<T, RecyclerViewHolder<T>>> {

    override fun create(
        recyclerViewHolderManager: RecyclerViewHolderManager<T, RecyclerViewHolder<T>>,
        itemDiffUtil: DiffUtil.ItemCallback<T>?
    ) = RecyclerViewAdapter(recyclerViewHolderManager, itemDiffUtil)
}