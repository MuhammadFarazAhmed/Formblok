package com.incubasys.formblok.common.callback

interface PaginatedListViewCallback : PagedRecyclerCallback, NetworkStateCallback {
    override fun onRetryButtonClicked()

    override fun onListItemClicked(position: Int)

    override fun onPageReload()
}