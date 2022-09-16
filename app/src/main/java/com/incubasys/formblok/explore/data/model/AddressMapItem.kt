package com.incubasys.formblok.explore.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

 class AddressMapItem(private var addressShort: AddressShort) : ClusterItem {

     val address :AddressShort
         get() = this.addressShort

    override fun getSnippet(): String {
        return ""
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getPosition(): LatLng {
        return LatLng(addressShort.lat, addressShort.lng)
    }
}