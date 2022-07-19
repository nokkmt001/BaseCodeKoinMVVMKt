package com.phat.testbase.dev.xbase.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

interface AdapterFactory<T : Any, out A : RecyclerView.Adapter<RecyclerViewHolder<T>>> {

    fun create(
        recyclerViewHolderManager: RecyclerViewHolderManager<T, RecyclerViewHolder<T>>,
        itemDiffUtil: DiffUtil.ItemCallback<T>?
    ): A
}