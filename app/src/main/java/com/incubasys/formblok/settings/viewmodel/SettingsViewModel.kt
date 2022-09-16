package com.incubasys.formblok.settings.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.data.AuthRepository
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.settings.data.ChangePasswordInput
import com.incubasys.formblok.settings.data.Settings
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    application: Application, private val parseErrors: ParseErrors
    , private val authRepository: AuthRepository
    , private val sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(application, parseErrors) {

    var list = mutableListOf<Settings>()
    var logoutLiveData = MutableLiveData<ApiResponse<Message>>()
    private var timer: Timer = Timer()
    private val delay: Long = 300 // milliseconds
    val changePasswordInput = ChangePasswordInput()
    var changePasswordMessage = MutableLiveData<ApiResponse<Message>>()

    init {
        setUpList()
    }

    private fun setUpList() {
        list.add(
            0,
            Settings(1, R.drawable.ic_change_password, "Change Password")
        )
        list.add(
            1,
            Settings(2, R.drawable.ic_privacy_policy, "Privacy Policy")
        )
        list.add(
            2, Settings(
                3,
                R.drawable.ic_privacy_policy,
                "Terms and Condition"
            )
        )
        list.add(3, Settings(4, R.drawable.ic_tutorial, "Tutorial"))
        list.add(4, Settings(5, R.drawable.ic_contact_admin, "Contact Admin"))

    }

    /**
     * ##################### For OldPassword Fragment ######################
     **/
    var changeOldPasswordError = ObservableField<String>("")
    var changeOldVisiblePasswordText = ObservableField<Boolean>(false)
    var changeOldEnablePasswordNextButton = ObservableBoolean(false)

    private fun validatePassword(): Boolean = changePasswordInput.oldPassword.length >= 8

    fun checkPasswordValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validatePassword()) {
                        changeOldPasswordError.set(null)
                        changeOldEnablePasswordNextButton.set(true)
                    } else {
                        changeOldPasswordError.set("Password Must be 8 characters long")
                        changeOldEnablePasswordNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!changePasswordInput.oldPassword.isEmpty()) {
            changeOldVisiblePasswordText.set(true)
        } else {
            changeOldVisiblePasswordText.set(false)
        }
    }

    /**
     * ##################### For NewPassword Fragment ######################
     **/
    var changeNewPasswordError = ObservableField<String>("")
    var changeNewVisiblePasswordText = ObservableField<Boolean>(false)
    var changeNewEnablePasswordNextButton = ObservableBoolean(false)

    private fun validateNewPassword(): Boolean = changePasswordInput.newPassword.length >= 8

    fun checkNewPasswordValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateNewPassword()) {
                        changeNewPasswordError.set(null)
                        changeNewEnablePasswordNextButton.set(true)
                    } else {
                        changeNewPasswordError.set("Password Must be 8 characters long")
                        changeNewEnablePasswordNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!changePasswordInput.newPassword.isEmpty()) {
            changeNewVisiblePasswordText.set(true)
        } else {
            changeNewVisiblePasswordText.set(false)
        }
    }


    fun logOut() {
        compositeDisposable.add(authRepository.logOut().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                logoutLiveData.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onComplete = {
                sharedPreferencesRepository.removeHeaders()
                sharedPreferencesRepository.setIsLoggedIn(false)
                logoutLiveData.value = ApiResponse(ApiStatus.SUCCESS, Message("Logout Success", 204))
            }, onError = {
                logoutLiveData.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
            })
        )
    }

    fun changePassword() {
        compositeDisposable.add(authRepository.changePassword(changePasswordInput)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                changePasswordMessage.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = {
                changePasswordMessage.value = ApiResponse(ApiStatus.SUCCESS, it)
            }, onError = {
                changePasswordMessage.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
            })
        )
    }
}
