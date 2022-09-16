package com.incubasys.formblok.home.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.databinding.ProfileListItemBinding
import com.incubasys.formblok.profile.data.model.Profile

class ProfileAdaptor : ListAdapter<Profile, ProfileAdaptor.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ProfileListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileAdaptor.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(itemViewBinding: ProfileListItemBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
        var binding = itemViewBinding
        fun bind(item: Profile) {
            binding.profile = item
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}