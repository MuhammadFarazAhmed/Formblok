package com.incubasys.formblok.common.data.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList


data class Listing<T>(
    val pagedListLiveData: LiveData<PagedList<T>>,
    val initLoadLiveData: LiveData<SimpleResponse>,
    val pageLoadLiveData: LiveData<SimpleResponse>
)