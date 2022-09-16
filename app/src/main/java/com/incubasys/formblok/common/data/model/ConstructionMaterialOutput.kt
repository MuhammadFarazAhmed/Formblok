package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConstructionMaterialOutput(
    @SerializedName("id") var id: Int? = -1, @SerializedName("name") var name: String = "", @SerializedName(
        "picture"
    ) var document: Document? = Document()
) :
    Parcelable {

}
