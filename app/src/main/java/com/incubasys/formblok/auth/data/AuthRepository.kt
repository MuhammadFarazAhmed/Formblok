package com.incubasys.formblok.auth.data

import com.incubasys.formblok.auth.data.api.AuthApi
import com.incubasys.formblok.common.data.model.Email
import com.incubasys.formblok.common.data.model.LoginInput
import com.incubasys.formblok.common.data.model.ResetPasswordInput
import com.incubasys.formblok.common.data.model.SignUpInput
import com.incubasys.formblok.settings.data.ChangePasswordInput
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApi: AuthApi) {

    fun checkEmailExists(email: String) = authApi.gETAuthCheckEmailDuplication(email)

    fun signUp(signUpInput: SignUpInput) = authApi.pOSTAuthSignup(signUpInput)

    fun signIn(loginInput: LoginInput) = authApi.pOSTAuthSignin(loginInput)

    fun requestResetPassword(email: String) = authApi.pOSTAuthResetPassword(Email(email))

    fun resetPassword(resetPasswordInput: ResetPasswordInput) = authApi.pUTAuthResetPassword(resetPasswordInput)

    fun changePassword(changePasswordInput: ChangePasswordInput) = authApi.changePassword(changePasswordInput)

    fun logOut() = authApi.logout()


}
