package com.incubasys.formblok.notification.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.PagedRecyclerCallback
import com.incubasys.formblok.common.data.pagination.BasePagedListAdapter
import com.incubasys.formblok.common.ui.LoaderViewHolder
import com.incubasys.formblok.databinding.LoaderListItemBinding
import com.incubasys.formblok.databinding.NotificationListItemBinding
import com.incubasys.formblok.notification.data.model.NotificationOutput
import kotlinx.android.synthetic.main.notification_list_item.view.*


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class NotificationAdapter(private var callback: PagedRecyclerCallback) :
    BasePagedListAdapter<NotificationOutput>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            ItemViewholder(NotificationListItemBinding.inflate(layoutInflater, parent, false))
        } else {
            LoaderViewHolder(LoaderListItemBinding.inflate(layoutInflater, parent, false), callback)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = getItem(position)
        if (getItemViewType(position) === TYPE_ITEM) {
            if (notification != null) {
                (holder as ItemViewholder).bind(notification)
            }
        } else {
            this.networkState?.let { (holder as LoaderViewHolder).bind(it) }
        }
    }

    inner class ItemViewholder(itemView: NotificationListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        private var binding = itemView
        fun bind(item: NotificationOutput) = with(itemView) {
            binding.notificationOutput = item
            binding.position = adapterPosition
            binding.callback = callback

            if (!item.isRead) {
                notification_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
            } else {
                notification_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOnPrimary))
            }

            setOnClickListener { callback.onListItemClicked(adapterPosition) }
        }

    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationOutput>() {
        override fun areItemsTheSame(oldItem: NotificationOutput, newItem: NotificationOutput): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationOutput, newItem: NotificationOutput): Boolean {
            return oldItem == newItem
        }
    }
}