package com.incubasys.formblok.common.data.remote

import com.incubasys.formblok.common.data.model.Message

class ApiResponse<T> {

    var status: ApiStatus

    var data: T? = null

    var error: Message? = null


    constructor(status: ApiStatus) {
        this.status = status
    }

    constructor(status: ApiStatus, data: T?) {
        this.status = status
        this.data = data
    }


    constructor(status: ApiStatus, error: Message) {
        this.status = status
        this.error = error
    }

    constructor(status: ApiStatus, data: T?, error: Message) {
        this.status = status
        this.data = data
        this.error = error
    }

    fun data(data: T?): ApiResponse<T> {
        this.data = data
        return this
    }

    fun error(error: Message): ApiResponse<T> {
        this.error = error
        return this
    }

    fun status(status: ApiStatus): ApiResponse<T> {
        this.status = status
        return this
    }
}