package com.incubasys.formblok.common.data.pagination

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.incubasys.formblok.common.data.model.SimpleResponse
import com.incubasys.formblok.common.data.remote.ApiStatus

abstract class BasePagedListAdapter<Item>(
    diffCallback: DiffUtil.ItemCallback<Item>
) :
    PagedListAdapter<Item, RecyclerView.ViewHolder>(diffCallback) {

    internal var networkState: SimpleResponse? = null

    override fun getItemViewType(position: Int) =
        if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }


    private fun hasExtraRow() = networkState != null
            && (networkState?.apiStatus === ApiStatus.LOADING
            || networkState?.apiStatus === ApiStatus.ERROR)

    fun setNetworkState(newNetworkState: SimpleResponse) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_PROGRESS = 1
    }

}