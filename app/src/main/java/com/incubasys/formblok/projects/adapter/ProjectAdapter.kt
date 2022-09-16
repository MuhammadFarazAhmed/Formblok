package com.incubasys.formblok.projects.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.common.callback.PagedRecyclerCallback
import com.incubasys.formblok.common.data.pagination.BasePagedListAdapter
import com.incubasys.formblok.common.ui.LoaderViewHolder
import com.incubasys.formblok.databinding.LoaderListItemBinding
import com.incubasys.formblok.databinding.ProjectListItemBinding
import com.incubasys.formblok.projects.data.model.Project

@Suppress("UNCHECKED_CAST", "DEPRECATED_IDENTITY_EQUALS")
class ProjectAdapter(private var callback: PagedRecyclerCallback) :
    BasePagedListAdapter<Project>(ProjectDiffCallback()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val project = getItem(position)
        if (getItemViewType(position) === TYPE_ITEM) {
            if (project != null) {
                (holder as ItemViewholder).bind(project)
            }
        } else {
            this.networkState?.let { (holder as LoaderViewHolder).bind(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            ItemViewholder(ProjectListItemBinding.inflate(layoutInflater, parent, false))
        } else {
            LoaderViewHolder(LoaderListItemBinding.inflate(layoutInflater, parent, false), callback)
        }
    }

    inner class ItemViewholder(itemView: ProjectListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
        fun bind(item: Project) {
            binding.project = item
            binding.position = adapterPosition
            binding.callback = callback
        }
    }
}

class ProjectDiffCallback : DiffUtil.ItemCallback<Project>() {

    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem == newItem
    }

}