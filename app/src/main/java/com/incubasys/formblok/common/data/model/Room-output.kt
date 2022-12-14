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
package com.incubasys.formblok.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

/**
 *
 * @param name
 * @param length
 * @param width
 */
@Parcelize
data class RoomOutput(
    val id: Int = -1,
    val icon: Document? = null,
    val createdAt: DateTime? = null,
    val name: String = "",
    var length: Double = 0.0,
    var width: Double = 0.0,
    @Transient var isChecked:Boolean = false,
    val myId : Long = System.currentTimeMillis()

) : Parcelable