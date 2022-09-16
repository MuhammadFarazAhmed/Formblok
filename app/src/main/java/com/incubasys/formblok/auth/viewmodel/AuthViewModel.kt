package com.incubasys.formblok.auth.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.auth.data.AuthRepository
import com.incubasys.formblok.auth.extentions.EmailValidator
import com.incubasys.formblok.common.data.model.*
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.util.ImagePicker
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class AuthViewModel @Inject constructor(
    application: Application,
    private val parseError: ParseErrors,
    private val authRepository: AuthRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(application, parseError) {

    lateinit var storeImagePath: String
    var imagePicker: ImagePicker = ImagePicker(application.applicationContext)
    var imagePath: String? = null
    var croppedImage = MutableLiveData<Bitmap>()

    var message: MutableLiveData<Message> = MutableLiveData()
    var forgotMessage: MutableLiveData<ApiResponse<Message>> = MutableLiveData()
    var resetMessage: MutableLiveData<ApiResponse<Message>> = MutableLiveData()

    var appLinkCode = MutableLiveData<String>()

    private var timer: Timer = Timer()
    private val delay: Long = 300 // milliseconds

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imagePath = imagePicker.getSelectedImagePath(requestCode, resultCode, data)
        if (imagePath != null) {
            this.storeImagePath = imagePath!!
        }
    }

    val signUpInput = SignUpInput()
    val loginInput = LoginInput()
    val resetPasswordInput = ResetPasswordInput()
    var forgotEmail: String? = null

    var signUpOutput: MutableLiveData<ApiResponse<SessionOutput>> = MutableLiveData()
    var loginOutput: MutableLiveData<ApiResponse<SessionOutput>> = MutableLiveData()
    var loginOutputWithNewPassword: MutableLiveData<ApiResponse<SessionOutput>> = MutableLiveData()

    /**
     *  ################### For Email Fragment #########################
     */
    var emailError = ObservableField("")
    var visibleEmailText = ObservableField(false)
    var enableEmailNextButton = ObservableBoolean(false)
    var showProgress = ObservableField(false)

    private fun validateEmail(): Boolean = signUpInput.email?.let { EmailValidator.isEmailValid(it) }!!

    fun checkEmailValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateEmail()) {
                        emailError.set(null)
                        checkEmailFromServer()
                    } else {
                        emailError.set("Valid Email Required")
                        enableEmailNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!signUpInput.email.isNullOrEmpty())
            visibleEmailText.set(true) else visibleEmailText.set(false)
    }

    private fun checkEmailFromServer() {
        compositeDisposable.add(
            authRepository.checkEmailExists(signUpInput.email?.trim()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    showProgress.set(true)
                }
                .subscribeBy(onNext = {
                    enableEmailNextButton.set(true)
                    showProgress.set(false)
                    message.value = Message(it.message(), it.raw().code())
                }, onError = {
                    showProgress.set(false)
                    message.value = parseError.interpretErrors(it)
                })
        )
        //observableSimpleCall(authRepository.checkEmailExists(signUpInput.email!!))
    }

    /**
     * ##################### For Create Password Fragment ######################
     **/
    var createPasswordError = ObservableField("")
    var visibleCreatePasswordText = ObservableField(false)
    var enableCreatePasswordNextButton = ObservableBoolean(false)

    private fun validateCreatePassword(): Boolean = signUpInput.password?.let { it.length >= 8 }!!

    fun checkCreatePasswordValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateCreatePassword()) {
                        createPasswordError.set(null)
                        enableCreatePasswordNextButton.set(true)
                    } else {
                        createPasswordError.set("Password Must be 8 characters long")
                        enableCreatePasswordNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!signUpInput.password.isNullOrEmpty()) {
            visibleCreatePasswordText.set(true)
        } else {
            visibleCreatePasswordText.set(false)
        }
    }


    /**
     * ##################### For Password Fragment ######################
     **/
    var passwordError = ObservableField("")
    var visiblePasswordText = ObservableField(false)
    var enablePasswordNextButton = ObservableBoolean(false)

    private fun validatePassword(): Boolean = loginInput.password?.let { it.length >= 8 }!!

    fun checkPasswordValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validatePassword()) {
                        passwordError.set(null)
                        enablePasswordNextButton.set(true)
                    } else {
                        passwordError.set("Password Must be 8 characters long")
                        enablePasswordNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!loginInput.password.isNullOrEmpty()) {
            visiblePasswordText.set(true)
        } else {
            visiblePasswordText.set(false)
        }
    }

    /**
     * ##################### For ForgotPassword Fragment ######################
     **/
    var forgotEmailError = ObservableField<String>("")
    var visibleForgotEmailText = ObservableField<Boolean>(false)
    var enableForgotEmailNextButton = ObservableBoolean(false)

    private fun validateForgotEmail(): Boolean = forgotEmail?.let { EmailValidator.isEmailValid(it) }!!

    fun checkForgotEmailValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateForgotEmail()) {
                        forgotEmailError.set(null)
                        enableForgotEmailNextButton.set(true)
                    } else {
                        forgotEmailError.set("Valid Email Required")
                        enableForgotEmailNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!forgotEmail.isNullOrEmpty())
            visibleForgotEmailText.set(true) else visibleForgotEmailText.set(false)
    }

    /**
     * ##################### For Create Password Fragment ######################
     **/
    var resetPasswordError = ObservableField<String>("")
    var visibleResetPasswordText = ObservableField<Boolean>(false)
    var enableResetPasswordNextButton = ObservableBoolean(false)

    private fun validateResetPassword(): Boolean = signUpInput.password?.let { it.length >= 8 }!!

    fun checkResetPasswordValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateResetPassword()) {
                        resetPasswordError.set(null)
                        enableResetPasswordNextButton.set(true)
                    } else {
                        resetPasswordError.set("Password Must be 8 characters long")
                        enableResetPasswordNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!signUpInput.password.isNullOrEmpty()) {
            visibleResetPasswordText.set(true)
        } else {
            visibleResetPasswordText.set(false)
        }
    }


    fun signUp() =
        compositeDisposable.add(authRepository.signUp(signUpInput)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                signUpOutput.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(
                onNext = {
                    it.user?.let { it1 -> saveUser(it, it1) }
                    signUpOutput.value = ApiResponse(ApiStatus.SUCCESS, it)
                }
                , onError = {
                    signUpOutput.value = ApiResponse(ApiStatus.ERROR, parseError.interpretErrors(it))
                }
            )
        )

    fun signIn() {
        loginInput.email = signUpInput.email
        compositeDisposable.add(authRepository.signIn(loginInput)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loginOutput.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(onNext = {
                it.user?.let { it1 -> saveUser(it, it1) }
                loginOutput.value = ApiResponse(ApiStatus.SUCCESS, it)
            }, onError = {
                loginOutput.value = ApiResponse(ApiStatus.ERROR, parseError.interpretErrors(it))
            })
        )
    }

    fun signInWithNewPassword() {
        loginInput.email = signUpInput.email
        loginInput.password = signUpInput.password
        compositeDisposable.add(authRepository.signIn(loginInput)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loginOutputWithNewPassword.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(onNext = {
                it.user?.let { it1 -> saveUser(it, it1) }
                loginOutputWithNewPassword.value = ApiResponse(ApiStatus.SUCCESS, it)
            }, onError = {
                loginOutputWithNewPassword.value = ApiResponse(ApiStatus.ERROR, parseError.interpretErrors(it))
            })
        )
    }

    private fun saveUser(
        it: SessionOutput,
        user: User
    ) {
        sharedPreferencesRepository.saveAuthHeaders(it.sid, it.stoken)
        sharedPreferencesRepository.setIsLoggedIn(true)
        it.user?.apply {
            sharedPreferencesRepository.setUserId(user.id)
            sharedPreferencesRepository.setUser(user)
        }
    }

    fun requestForgotPassword() = compositeDisposable.add(authRepository.requestResetPassword(forgotEmail.toString())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
            forgotMessage.value = ApiResponse(ApiStatus.LOADING)
        }
        .subscribeBy(onNext = {
            forgotMessage.value = ApiResponse(ApiStatus.SUCCESS, it)
        }, onError = {
            forgotMessage.value = ApiResponse(ApiStatus.ERROR, parseError.interpretErrors(it))
        })
    )

    fun resetPassword() {
        resetPasswordInput.password = signUpInput.password
        compositeDisposable.add(authRepository.resetPassword(resetPasswordInput)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                resetMessage.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(onNext = {
                resetMessage.value = ApiResponse(ApiStatus.SUCCESS, it)
            }, onError = {
                resetMessage.value = ApiResponse(ApiStatus.ERROR, parseError.interpretErrors(it))
            })
        )
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }


}
