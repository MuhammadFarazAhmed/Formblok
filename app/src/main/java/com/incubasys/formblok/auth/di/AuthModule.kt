package com.incubasys.formblok.auth.di

import com.incubasys.formblok.auth.data.AuthRepository
import com.incubasys.formblok.auth.data.api.AuthApi
import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.util.ParseErrors
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @Provides
    @ActivityScope
    fun authApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @ActivityScope
    fun authRepository(authApi: AuthApi): AuthRepository {
        return AuthRepository(authApi)
    }
}