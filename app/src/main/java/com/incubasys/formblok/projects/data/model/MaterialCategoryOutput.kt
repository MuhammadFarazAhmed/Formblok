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
import com.incubasys.formblok.common.data.model.MaterialTypeProperty
import kotlinx.android.parcel.Parcelize


/**
 * 
 * @param id 
 * @param name 
 * @param price 
 * @param categoryOrder 
 */
@Parcelize
data class MaterialCategoryOutput (
    var id: Int = -1,
    var name:String ="",
    var price: String ="",
    var categoryOrder: Int = -1,
    var totalQuotePrice : Double = 0.0,
    val types: MutableList<MaterialTypeProperty> = mutableListOf()

) : Parcelable {
}