package com.incubasys.formblok.common.callback

interface PagedRecyclerCallback : RecyclerViewCallback {
    fun onPageReload()
    override fun onListItemClicked(position: Int)
}