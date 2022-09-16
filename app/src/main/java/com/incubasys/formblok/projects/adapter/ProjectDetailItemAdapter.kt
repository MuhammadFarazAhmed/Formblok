package com.incubasys.formblok.projects.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.databinding.ProjectDetailItemListHeaderBinding
import com.incubasys.formblok.databinding.ProjectDetailItemListNormalBinding
import com.incubasys.formblok.projects.data.model.ProjectDetailItem

class ProjectDetailItemAdapter :
    ListAdapter<ProjectDetailItem, ProjectDetailItemAdapter.ItemViewholder>(ProjectDetailDiffCallback()) {

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
                ProjectDetailItemListHeaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
            TYPE_NORMAL -> NormalItemViewholder(
                ProjectDetailItemListNormalBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> {
                HeaderItemViewholder(
                    ProjectDetailItemListHeaderBinding
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
        fun bindViews(projectDetailItem: ProjectDetailItem)
    }

    open class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView)

    class HeaderItemViewholder(itemView: ProjectDetailItemListHeaderBinding) : ItemViewholder(itemView.root),
        UpdateViewHolder {
        var binding = itemView
        override fun bindViews(projectDetailItem: ProjectDetailItem) {
            binding.projectItemDetailHeader = projectDetailItem
        }
    }

    class NormalItemViewholder(itemView: ProjectDetailItemListNormalBinding) : ItemViewholder(itemView.root),
        UpdateViewHolder {
        var binding = itemView
        override fun bindViews(projectDetailItem: ProjectDetailItem) {
            binding.projectItemDetailNormal = projectDetailItem
        }
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_NORMAL = 1
    }
}

class ProjectDetailDiffCallback : DiffUtil.ItemCallback<ProjectDetailItem>() {
    override fun areItemsTheSame(oldItem: ProjectDetailItem, newItem: ProjectDetailItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProjectDetailItem, newItem: ProjectDetailItem): Boolean {
        return oldItem == newItem
    }
}