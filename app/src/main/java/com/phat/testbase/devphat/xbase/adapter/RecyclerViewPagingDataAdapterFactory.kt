package com.phat.testbase.devphat.xbase.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class RecyclerViewPagingDataAdapterFactory<T : Any> :
    AdapterFactory<T, PagingDataAdapter<T, RecyclerViewHolder<T>>> {

    override fun create(
        recyclerViewHolderManager: RecyclerViewHolderManager<T, RecyclerViewHolder<T>>,
        itemDiffUtil: DiffUtil.ItemCallback<T>?
    ) = RecyclerViewPagingDataAdapter(recyclerViewHolderManager, itemDiffUtil)
}