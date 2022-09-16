package com.incubasys.formblok.profile.data

import com.incubasys.formblok.common.data.model.ProfileInput
import com.incubasys.formblok.common.data.repository.BaseRepository
import com.incubasys.formblok.profile.data.api.ProfileApi
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileApi: ProfileApi) : BaseRepository() {

    fun updateProfile(profileInput: ProfileInput) = profileApi.pUtUpdateProfile(profileInput)
}