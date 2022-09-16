package com.incubasys.formblok.common.ui

import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.common.callback.PagedRecyclerCallback
import com.incubasys.formblok.common.data.model.SimpleResponse

class LoaderViewHolder(private val binding: com.incubasys.formblok.databinding.LoaderListItemBinding, callback: PagedRecyclerCallback) :
    RecyclerView.ViewHolder(binding.root) {

    val callback: PagedRecyclerCallback
        get() = binding.callback!!

    val networkState: SimpleResponse
        get() = binding.data!!

    init {
        this.binding.callback = callback
        binding.executePendingBindings()
    }

    fun bind(networkState: SimpleResponse) {
        binding.data = networkState
    }
}
