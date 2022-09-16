package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.incubasys.formblok.projects.data.model.Project
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectPage(
    @SerializedName("page_info")
    var pageInfo: PageInfo? = null,
    @SerializedName("projects")
    var projects: MutableList<Project>? = null
) : Parcelable {

}