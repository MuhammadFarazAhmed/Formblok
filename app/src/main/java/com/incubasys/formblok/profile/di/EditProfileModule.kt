package com.incubasys.formblok.profile.di

import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.profile.data.ProfileRepository
import com.incubasys.formblok.profile.data.api.ProfileApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class EditProfileModule {

    @Provides
    @ActivityScope
    fun profileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Provides
    @ActivityScope
    fun provideProfileRepository(profileApi: ProfileApi) =
        ProfileRepository(profileApi)
}