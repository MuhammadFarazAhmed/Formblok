package com.incubasys.formblok.common.data.model

import com.incubasys.formblok.common.data.remote.ApiStatus

data class SimpleResponse(var apiStatus: ApiStatus, var message: String = "") {

}