package com.incubasys.formblok.common.data.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.incubasys.formblok.common.data.model.AuthHeader
import com.incubasys.formblok.common.data.model.User
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val gson: Gson,
    private val prefs: SharedPreferences
) :
    BaseRepository() {

    private val isLoggedInLiveData = MutableLiveData<Boolean>()

    init {
        isLoggedInLiveData.value = getAuthHeaders().checkNull()
    }

    fun saveAuthHeaders(sid: Int, stoken: String) =
        prefs.apply {
            edit().putInt(sidKEY, sid).apply()
            edit().putString(stokenKEY, stoken).apply()
        }

    fun isLoggedIn() = prefs.getBoolean(isLoggedInKEY, false)

    fun setIsLoggedIn(isLoggedIn: Boolean) {
        isLoggedInLiveData.value = isLoggedIn
        prefs.edit().putBoolean(isLoggedInKEY, isLoggedIn).apply()
    }

    fun isOnboardingShown() = prefs.getBoolean(isonboardingshownKEY, false)

    fun setIsOnboardingShown(isFirstTime: Boolean) = prefs.edit().putBoolean(isonboardingshownKEY, isFirstTime).apply()

    fun getUserId(): Int =
        prefs.getInt(KEY_userId, -1)

    fun setUserId(userId: Int) {
        prefs.edit().putInt(KEY_userId, userId).apply()
    }

    fun getUser(): User {
        val json = prefs.getString(KEY_user, "{}")
        return gson.fromJson<User>(json, User::class.java)
    }

    fun setUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString(KEY_user, json).apply()
    }

    fun removeUser(){
        prefs.edit().remove(KEY_user).apply()
    }


    fun getAuthHeaders(): AuthHeader {
        val tempSid = prefs.getInt(sidKEY, -1)
        val sid = if (tempSid == -1) null else tempSid
        val stoken = prefs.getString(stokenKEY, null)
        return AuthHeader(sid, stoken)
    }

    fun isLoggedInObservable(): LiveData<Boolean> {
        return isLoggedInLiveData
    }

    fun removeHeaders() {
        prefs.edit().remove(sidKEY).apply()
        prefs.edit().remove(stokenKEY).apply()
    }

    companion object {
        private const val sidKEY = "sid"
        private const val stokenKEY = "stoken"
        private const val isonboardingshownKEY = "isOnboardingShown"
        private const val isLoggedInKEY = "isLoggedIn"
        private const val KEY_userId = "KEY_userId"
        private const val KEY_user = "KEY_user"
    }
}