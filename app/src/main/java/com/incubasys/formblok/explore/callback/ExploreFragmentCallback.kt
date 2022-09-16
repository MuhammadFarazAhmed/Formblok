package com.incubasys.formblok.explore.callback

import com.google.android.gms.maps.model.LatLng

interface ExploreFragmentCallback {
    fun requestLocation()
    fun onAddressClicked(projectId: Int? = -1,addressId: Int,latLng: LatLng)
}