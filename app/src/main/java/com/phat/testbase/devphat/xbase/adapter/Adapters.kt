package com.phat.testbase.devphat.xbase.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.paging.PagingDataAdapter


inline fun <T : Any> adapterOf(
    adapterBuilder: AdapterBuilder<T>.() -> Unit
): ListAdapter<T, RecyclerViewHolder<T>> =
    AdapterBuilder<T>().apply(adapterBuilder).build(
        RecyclerViewAdapterFactory()
    ) as ListAdapter<T, RecyclerViewHolder<T>>

inline fun <T : Any> pagingDataAdapterOf(
    adapterBuilder: AdapterBuilder<T>.() -> Unit
): PagingDataAdapter<T, RecyclerViewHolder<T>> =
    AdapterBuilder<T>().apply(adapterBuilder).build(
        RecyclerViewPagingDataAdapterFactory()
    ) as PagingDataAdapter<T, RecyclerViewHolder<T>>