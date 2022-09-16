package com.incubasys.formblok.auth.extentions

class EmailValidator {
    companion object {
        @JvmStatic
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

        fun isEmailValid(email: String): Boolean = EMAIL_REGEX.toRegex().matches(email);
    }
}