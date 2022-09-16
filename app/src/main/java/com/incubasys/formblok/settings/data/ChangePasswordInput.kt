package com.incubasys.formblok.settings.data

import com.google.gson.annotations.SerializedName

data class ChangePasswordInput(@SerializedName("old_password") var oldPassword: String = "", @SerializedName("new_password") var newPassword: String = "") {

}