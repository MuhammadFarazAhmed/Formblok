package com.incubasys.formblok.projects.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NeighbourhoodInfo(
    var school: NearbyBuilding = NearbyBuilding(),
    var park: NearbyBuilding = NearbyBuilding(),
    var hospital: NearbyBuilding = NearbyBuilding(),
    var emergency_services: NearbyBuilding = NearbyBuilding(),
    var public_transport: NearbyBuilding = NearbyBuilding()
) : Parcelable {

}
