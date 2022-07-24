package com.phat.testbase.dev.xbase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.phat.testbase.databinding.ItemPosterBinding
import com.phat.testbase.model.Poster

class TestAdapter : BaseBindingAdapter<Poster, ItemPosterBinding, TestAdapter.TestVH>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val bind = ItemPosterBinding.inflate(LayoutInflater.from(parent.context))
        return TestVH(bind)
    }

    override fun onBindViewHolder(holder: TestVH, position: Int) {

    }

    class TestVH(bind: ItemPosterBinding) : BaseBindingVH<ItemPosterBinding>(bind){

        override fun bind(item: ItemPosterBinding) {

        }

    }
}