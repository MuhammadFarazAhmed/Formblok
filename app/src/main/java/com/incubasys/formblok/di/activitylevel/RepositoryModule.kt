package com.incubasys.formblok.di.activitylevel

import android.content.SharedPreferences
import com.google.gson.Gson
import com.incubasys.formblok.common.data.remote.api.UserApi
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.common.data.repository.UserRepository
import com.incubasys.formblok.di.scope.AppScope
import com.incubasys.formblok.explore.data.api.ExploreApi
import com.incubasys.formblok.notification.data.api.NotificationApi
import com.incubasys.formblok.projects.data.api.AddressApi
import com.incubasys.formblok.projects.data.api.ProjectApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [ViewModelModule::class])
class RepositoryModule {
    // Here we provide a common repository that has @AppScope and used by multiple modules(by modules I mean Activity)
    @Provides
    @AppScope
    fun userRepository(
        userApi: UserApi
    ) =
        UserRepository(userApi)

    @Provides
    @AppScope
    fun provideSharedPreferencesRepository(gson: Gson, sharedPreferences: SharedPreferences) =
        SharedPreferencesRepository(gson, sharedPreferences)


    @Provides
    @AppScope
    fun notificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @AppScope
    fun exploreApi(retrofit: Retrofit): ExploreApi {
        return retrofit.create(ExploreApi::class.java)
    }

    @Provides
    @AppScope
    fun projectApi(retrofit: Retrofit): ProjectApi {
        return retrofit.create(ProjectApi::class.java)
    }

    @Provides
    @AppScope
    fun addressApi(retrofit: Retrofit): AddressApi {
        return retrofit.create(AddressApi::class.java)
    }

}
