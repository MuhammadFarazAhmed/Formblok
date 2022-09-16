package com.incubasys.formblok.profile.callback

interface EditProfileFragmentCallback {

    fun onBackPress()
    fun onChangePhotoClicked()
    fun onNameItemClicked()
    fun onEmailItemClicked()
    fun onGenderItemClicked()
    fun onDobItemClicked()
    fun onSaveButtonClicked()
}