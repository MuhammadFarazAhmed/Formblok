package com.incubasys.formblok.splash.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.data.model.AuthHeader
import com.incubasys.formblok.common.data.model.SessionOutput
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.data.repository.UserRepository
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.util.ParseErrors
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val parseErrors: ParseErrors,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : BaseViewModel(Application(), parseErrors) {

    val sessionOutput = MutableLiveData<ApiResponse<SessionOutput>>()

    fun isOnboardingShown() =
        sharedPreferencesRepository.isOnboardingShown()

    fun getAuthHeaders() =
        sharedPreferencesRepository.getAuthHeaders()

    fun isLoggedIn() = sharedPreferencesRepository.isLoggedIn()

    fun removeUserLoginInfo(){
        sharedPreferencesRepository.removeHeaders()
        sharedPreferencesRepository.removeUser()
    }

    fun validateSession() = compositeDisposable.add(userRepository.validateSession()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(onNext = {
            sharedPreferencesRepository.saveAuthHeaders(it.sid, it.stoken)
            it?.user?.apply {
                sharedPreferencesRepository.setUserId(it.user.id)
                sharedPreferencesRepository.setUser(it.user)
            }
            sessionOutput.value = ApiResponse(ApiStatus.SUCCESS, it)
        }, onError = {
            sessionOutput.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))

        })
    )
}
