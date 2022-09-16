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

import com.google.gson.annotations.SerializedName


/**
 * 
 * @param name 
 * @param property 
 */
data class ProjectInput (

    @SerializedName("name") var name: String = "",
    @SerializedName("property") var property: PropertyInput? = null
) {
}