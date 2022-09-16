package com.incubasys.formblok.quotation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.common.data.model.RoomOutput
import com.incubasys.formblok.databinding.RoomListItemBinding
import com.incubasys.formblok.quotation.callback.RoomAdapterCallback

class RoomAdapter(
    private val context: Context,
    private val callback: RoomAdapterCallback
) :
    ListAdapter<RoomOutput, RoomAdapter.ItemViewholder>(RoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            RoomListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    /*override fun onViewAttachedToWindow(holder: ItemViewholder) {
        holder.setIsRecyclable(false)
        super.onViewAttachedToWindow(holder)
    }*/

    inner class ItemViewholder(itemView: RoomListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView

        init {
            binding.cvRoomAreaDetail.setOnClickListener {
                callback.onItemClicked(adapterPosition)
            }

            binding.rbRoomListItemRadio.setOnClickListener {
                callback.onItemCheckChanged(getItem(adapterPosition), adapterPosition)
            }

            binding.ivRoomListItemAddIcon.setOnClickListener {
                if (getItem(adapterPosition).isChecked) {
                    callback.onItemAddClicked(adapterPosition, getItem(adapterPosition))
                } else {
                    Toast.makeText(context, "First check this item", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun bind(item: RoomOutput) {
            binding.room = item
        }

    }

    class RoomDiffCallback : DiffUtil.ItemCallback<RoomOutput>() {
        override fun areItemsTheSame(oldItem: RoomOutput, newItem: RoomOutput): Boolean {
            return oldItem.myId == newItem.myId
        }

        override fun areContentsTheSame(oldItem: RoomOutput, newItem: RoomOutput): Boolean {
            return oldItem == newItem
        }
    }

}