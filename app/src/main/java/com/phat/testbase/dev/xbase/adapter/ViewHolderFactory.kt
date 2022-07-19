package com.phat.testbase.dev.xbase.adapter

import android.view.View

typealias ViewHolderCreator<VH> = (view: View) -> VH

abstract class ViewHolderFactory<out T : Any, out VH : RecyclerViewHolder<T>> {
    abstract fun instantiate(view: View): VH
}