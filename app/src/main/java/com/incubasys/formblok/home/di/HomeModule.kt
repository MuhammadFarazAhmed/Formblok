package com.incubasys.formblok.home.di

import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.explore.data.ExploreRepository
import com.incubasys.formblok.explore.data.api.ExploreApi
import com.incubasys.formblok.notification.data.NotificationRepository
import com.incubasys.formblok.notification.data.api.NotificationApi
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.api.ProjectApi
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    @ActivityScope
    fun provideProjectRepository(projectApi: ProjectApi) =
        ProjectsRepository(projectApi)

    @Provides
    @ActivityScope
    fun provideNotificationRepository(notificationApi: NotificationApi) =
        NotificationRepository(notificationApi)

    @Provides
    @ActivityScope
    fun provideExploreRepository(exploreApi: ExploreApi) = ExploreRepository(exploreApi)

}