package com.incubasys.formblok.quotation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.databinding.FragmentZoneListDialogItemBinding
import com.incubasys.formblok.projects.data.model.Zone

class ZoneHorizontalAdapter : ListAdapter<Zone, ZoneHorizontalAdapter.ItemViewholder>(
    ZoneHorizontalDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            FragmentZoneListDialogItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(itemView: FragmentZoneListDialogItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
        fun bind(item: Zone) = with(itemView) {
            binding.zone = item
            setOnClickListener {

            }
        }
    }
}

class ZoneHorizontalDiffCallback : DiffUtil.ItemCallback<Zone>() {
    override fun areItemsTheSame(oldItem: Zone, newItem: Zone): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Zone, newItem: Zone): Boolean {
        return oldItem == newItem
    }
}