package com.phat.testbase.dev.xbase.adapter5

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class TypesViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val viewDataBinding: ViewDataBinding? = DataBindingUtil.bind(itemView)

    var item: T? = null
}
