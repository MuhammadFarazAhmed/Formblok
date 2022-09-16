package com.incubasys.formblok.projects.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.RecyclerViewCallback
import com.incubasys.formblok.databinding.PropertyListItemBinding
import com.incubasys.formblok.projects.data.model.PropertyMinimal

class PropertyAdapter(private val callback: RecyclerViewCallback) :
    ListAdapter<PropertyMinimal, PropertyAdapter.ItemViewholder>(PropertyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewholder(
            PropertyListItemBinding.inflate(inflater,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(itemView: PropertyListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(item: PropertyMinimal) {
            binding.property = item
            binding.callback = callback
            binding.position = adapterPosition
        }
    }
}

class PropertyDiffCallback : DiffUtil.ItemCallback<PropertyMinimal>() {
    override fun areItemsTheSame(oldItem: PropertyMinimal, newItem: PropertyMinimal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PropertyMinimal, newItem: PropertyMinimal): Boolean {
        return oldItem == newItem
    }
}