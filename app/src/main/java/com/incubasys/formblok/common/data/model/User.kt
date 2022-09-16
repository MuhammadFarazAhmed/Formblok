package com.incubasys.formblok.common.data.model

import org.joda.time.LocalDate

data class
User(
    val id: Int,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    val isAgent: Boolean,
    val isUser: Boolean,
    val isAdmin: Boolean = false,
    val isBuyer: Boolean
    ,
    val dob: LocalDate? = null,
    val photo: Document? = null,
    /* 0 => Female 1 => Male 2 => Neutral */
    val gender: Int? = null
)
