package com.incubasys.formblok.settings.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.databinding.SettingsListItemBinding
import com.incubasys.formblok.profile.callback.EditProfileAdaptorCallback
import com.incubasys.formblok.settings.callback.SettingsAdaptorCallback
import com.incubasys.formblok.settings.data.Settings

class SettingsAdapter : ListAdapter<Settings, SettingsAdapter.ItemViewholder>(DiffCallback()) {

    internal var callback: SettingsAdaptorCallback? = null

    fun setAdaptorCallback(callback: SettingsAdaptorCallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            SettingsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SettingsAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(itemView: com.incubasys.formblok.databinding.SettingsListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
        fun bind(item: Settings) {
            binding.setting = item
            with(itemView) {
                setOnClickListener {
                    callback?.onListItemClicked(adapterPosition)
                }
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Settings>() {
    override fun areItemsTheSame(oldItem: Settings, newItem: Settings): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Settings, newItem: Settings): Boolean {
        return oldItem == newItem
    }
}