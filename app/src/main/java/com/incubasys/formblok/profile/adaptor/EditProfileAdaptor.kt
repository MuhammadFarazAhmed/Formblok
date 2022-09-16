package com.incubasys.formblok.profile.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.databinding.EditProfileListItemBinding
import com.incubasys.formblok.profile.callback.EditProfileAdaptorCallback
import com.incubasys.formblok.profile.data.model.Profile

class EditProfileAdaptor : ListAdapter<Profile, EditProfileAdaptor.ItemViewholder>(DiffCallback()) {

    internal var callback: EditProfileAdaptorCallback? = null

    fun setAdaptorCallback(callback: EditProfileAdaptorCallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            EditProfileListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EditProfileAdaptor.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(itemView: EditProfileListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
        fun bind(item: Profile) {
            binding.profile = item
            with(itemView) {
                setOnClickListener {
                    callback?.onListItemClicked(adapterPosition)
                }
            }
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