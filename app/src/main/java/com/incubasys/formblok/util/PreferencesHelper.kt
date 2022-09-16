package com.incubasys.formblok.util

import android.content.SharedPreferences
import com.incubasys.formblok.common.data.model.AuthHeader

import javax.inject.Inject

class PreferencesHelper @Inject
constructor(private val prefs: SharedPreferences) {

    private val KEY_isonboardingshown = "isOnboardingShown"
    private val KEY_verified = "KEY_verified"
    private val KEY_userId = "KEY_userId"
    private val KEY_sid = "sid"
    private val KEY_stoken = "stoken"

    val authHeaders: AuthHeader
        get() {
            val tempSid = prefs.getInt(KEY_sid, -1)
            val sid = if (tempSid == -1) null else tempSid
            val stoken = prefs.getString(KEY_stoken, null)
            return AuthHeader(sid!!, stoken!!)
        }


    var userId: Int
        get() = prefs.getInt(KEY_userId, -1)
        set(userId) = prefs.edit().putInt(this.KEY_userId, userId).apply()

    var isUserVerified: Boolean
        get() = prefs.getBoolean(KEY_verified, false)
        set(isVerified) = prefs.edit().putBoolean(KEY_verified, isVerified).apply()

    var isOnboardingShown: Boolean
        get() = prefs.getBoolean(KEY_isonboardingshown, false)
        set(isFirstTime) = prefs.edit().putBoolean(KEY_isonboardingshown, isFirstTime).apply()

    var profilePhoto: String?
        get() = prefs.getString("profilePhoto", null)
        set(imagePath) = prefs.edit().putString("profilePhoto", imagePath).apply()

    var isCoachMarksEnabled: Boolean
        get() = prefs.getBoolean("isCoachMarks", true)
        set(isChecked) = prefs.edit().putBoolean("isCoachMarks", isChecked).apply()

    var coachMarkStatus: String?
        get() = prefs.getString("coachMarks", null)
        set(coachMarkStatus) = prefs.edit().putString("coachMarks", coachMarkStatus).apply()

    var acceptTerms: Boolean
        get() = prefs.getBoolean("terms", false)
        set(b) = prefs.edit().putBoolean("terms", b).apply()

    fun saveAuthHeaders(sid: Int?, stoken: String) {
        prefs.edit().putInt(KEY_sid, sid!!).apply()
        prefs.edit().putString(KEY_stoken, stoken).apply()

    }

    fun removeHeaders() {
        prefs.edit().remove(KEY_sid).apply()
        prefs.edit().remove(KEY_stoken).apply()
    }

    fun removeUserInfo() {
        prefs.edit().remove(KEY_verified).apply()
        prefs.edit().remove(KEY_userId).apply()
        prefs.edit().remove("profilePhoto").apply()
    }

    fun removeProfilePhoto() {
        prefs.edit().remove("profilePhoto").apply()
    }
}
