package com.incubasys.formblok.quotation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.databinding.MaterialHeaderItemBinding
import com.incubasys.formblok.databinding.MaterialNormalItemBinding
import com.incubasys.formblok.quotation.data.model.Material

class MaterialAdapter : ListAdapter<Material, MaterialAdapter.ItemViewholder>(MaterialDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            0 -> TYPE_HEADER
            1 -> TYPE_NORMAL
            else -> {
                1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return when (viewType) {
            TYPE_HEADER -> HeaderItemViewholder(
                MaterialHeaderItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
            TYPE_NORMAL -> NormalItemViewholder(
                MaterialNormalItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> {
                HeaderItemViewholder(
                    MaterialHeaderItemBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> (holder as HeaderItemViewholder).bindViews(getItem(position))
            TYPE_NORMAL -> (holder as NormalItemViewholder).bindViews(getItem(position))
        }
    }

    interface UpdateViewHolder {
        fun bindViews(item: Material)
    }

    open class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView)

    class HeaderItemViewholder(itemView: MaterialHeaderItemBinding) : ItemViewholder(itemView.root),
        UpdateViewHolder {
        var binding = itemView
        override fun bindViews(item: Material) {
            binding.material = item
        }
    }

    class NormalItemViewholder(itemView: MaterialNormalItemBinding) : ItemViewholder(itemView.root),
        UpdateViewHolder {
        var binding = itemView
        override fun bindViews(item: Material) {
            binding.rvMaterialSubList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val subAdapter = HorizontalAdapter()
                adapter = subAdapter
                subAdapter.submitList(item.list)
            }
        }
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_NORMAL = 1
    }
}

class MaterialDiffCallback : DiffUtil.ItemCallback<Material>() {
    override fun areItemsTheSame(oldItem: Material, newItem: Material): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Material, newItem: Material): Boolean {
        return oldItem == newItem
    }
}