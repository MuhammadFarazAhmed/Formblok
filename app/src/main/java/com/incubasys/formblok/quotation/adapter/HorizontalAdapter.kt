package com.incubasys.formblok.quotation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.common.data.model.ConstructionMaterialOutput
import com.incubasys.formblok.databinding.HorizontalListItemBinding

class HorizontalAdapter :
    ListAdapter<ConstructionMaterialOutput, HorizontalAdapter.ItemViewholder>(HorizontalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            HorizontalListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(itemView: HorizontalListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
        fun bind(item: ConstructionMaterialOutput) {
            binding.construction = item
        }
    }
}

class HorizontalDiffCallback : DiffUtil.ItemCallback<ConstructionMaterialOutput>() {
    override fun areItemsTheSame(oldItem: ConstructionMaterialOutput, newItem: ConstructionMaterialOutput): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ConstructionMaterialOutput, newItem: ConstructionMaterialOutput): Boolean {
        return oldItem == newItem
    }
}