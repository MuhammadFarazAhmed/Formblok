package com.incubasys.formblok.common.data.model

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDate


/**
 *
 * @param name
 * @param dob
 * @param gender 0 => Female 1 => Male 2 => Indifferent
 * @param isAgent 0 => Buyer 1 =>  Agent 2 => Both
 * @param isBuyer
 * @param photoInput
 * @param email
 */
class ProfileInput(
    @SerializedName("name") var name: kotlin.String? = null,
    @SerializedName("photo_input") var photoInput: kotlin.String? = null
    ,
    @SerializedName("dob") var dob: String? = null,
    /* 0 => Female 1 => Male 2 => Indifferent */
    @SerializedName("gender") var gender: kotlin.Int? = null,
    /* 0 => Buyer 1 =>  Agent 2 => Both */
    @SerializedName("is_agent") var isAgent: kotlin.Boolean? = null,
    @SerializedName("is_buyer") var isBuyer: kotlin.Boolean? = null,
    @SerializedName("email") var email: kotlin.String? = null
) {
}