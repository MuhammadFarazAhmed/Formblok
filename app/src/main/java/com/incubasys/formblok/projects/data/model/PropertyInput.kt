/**
 * Formblok Mobile
 * API documentation for the mobile endpoints of formblok.
 *
 * OpenAPI spec version: v1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package com.incubasys.formblok.projects.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PropertyInput(
    @SerializedName("lot_number") var lot_number: String = "",
    @SerializedName("property_number") var property_number: String = "",
    @SerializedName("site_area") var site_area: Double = 0.0,
    @SerializedName("garden_area") var garden_area: Double = 0.0,
    @SerializedName("garden_area_percentage") var garden_area_percentage: Double = 0.0,
    @SerializedName("north_area") var north_area: Double = 0.0,
    @SerializedName("north_area_percentage") var north_area_percentage: Double = 0.0,
    @SerializedName("driveway_area") var driveway_area: Double = 0.0,
    @SerializedName("open_liveable_area") var open_liveable_area: Double = 0.0,
    @SerializedName("ground_floor_area") var ground_floor_area: Double = 0.0,
    @SerializedName("total_liveable_area") var total_liveable_area: Double = 0.0,
    @SerializedName("floors") var floors: Int = -1,
    @SerializedName("construction_style") var construction_style: Int = 0,
    @SerializedName("construction_type") var construction_type: Int = -1,
    @SerializedName("development_type") var development_type: Int = -1,
    @SerializedName("first_floor_area") var first_floor_area: Double = 0.0,
    @SerializedName("already_built_area") var already_built_area: Double? = null,
    @SerializedName("demolish_area") var demolish_area: Double? = null,
    @SerializedName("material_category_id") var materialCategoryId: Int? = null,
    @SerializedName("property_rooms") var property_rooms: MutableList<PropertyRoomInput>? = mutableListOf(),
    @SerializedName("street_address") var address: String = "",
    @SerializedName("area") var area: String = "",
    @SerializedName("total_cost") var total_cost: Double = 0.0,
    @SerializedName("address_id") var addressId: Int = -1
) : Parcelable