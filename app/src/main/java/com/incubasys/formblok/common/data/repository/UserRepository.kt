package com.incubasys.formblok.common.data.repository

import com.incubasys.formblok.common.data.remote.api.UserApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) :
    BaseRepository() {

    fun validateSession() = userApi.gETAuthValidateSession()

}