package com.incubasys.formblok.common.data.pagination

import androidx.paging.DataSource
import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.util.ParseErrors

abstract class BaseDataSourceFactory<Item, API>(open var parseErrors: ParseErrors, var api: API) :
    DataSource.Factory<PageInfo, Item>()