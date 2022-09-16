package com.incubasys.formblok.projects.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Polygon(
    var lat: Double = 0.0,
    var lng: Double = 0.0
) : Parcelable {

    fun toLatLng():LatLng{
        return LatLng(lat,lng)
    }
}
