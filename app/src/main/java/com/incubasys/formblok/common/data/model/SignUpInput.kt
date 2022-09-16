package com.incubasys.formblok.common.data.model

import com.google.gson.annotations.SerializedName

class SignUpInput(
    @SerializedName("email") var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("name")
     var name: String? = null,
    @SerializedName("dob")
     var dob: String? = null,
    @SerializedName("photo_input")
     var photoInput: String? = null,
    @SerializedName("gender")
     var gender: Int? = null,
    @SerializedName("is_agent")
     var isAgent: Boolean? = false,
    @SerializedName("is_buyer")
     var isBuyer: Boolean? = false
)
