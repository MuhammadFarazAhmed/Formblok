package com.incubasys.formblok.projects.data.model

data class ProjectDetailItem(
    var id: Int? = null,
    var type: Int? = 1,
    var upperText: String? = "",
    var lowerText: String? = "",
    var isShownDistance: Boolean = false,
    var distance: String = "",
    var isSpanned: Boolean = false,
    var isPercentageShown: Boolean = false,
    var areaPercentage: Double = 0.0,
    var specialTextView: Boolean = true
)