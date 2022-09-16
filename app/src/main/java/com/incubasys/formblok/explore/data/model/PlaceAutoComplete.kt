package com.incubasys.formblok.explore.data.model

data class PlaceAutocomplete(
    var placeId: CharSequence = "",
    var area: CharSequence = "",
    var address: CharSequence = ""
) {

    override fun toString(): String {
        return area.toString()
    }
}