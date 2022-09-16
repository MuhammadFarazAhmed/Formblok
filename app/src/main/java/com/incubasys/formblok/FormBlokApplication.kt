package com.incubasys.formblok

import android.content.Context
import androidx.lifecycle.Observer
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.incubasys.formblok.common.data.remote.client.SessionAuth
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.di.AppComponent
import com.incubasys.formblok.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric.with
import javax.inject.Inject

public class FormBlokApplication : DaggerApplication() {

    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    private var isLoggedInCallback = Observer<Boolean> {
        setHeaders()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        sharedPreferencesRepository.isLoggedInObservable().observeForever(isLoggedInCallback)
        return appComponent
    }


    override fun onCreate() {
        super.onCreate()
        with(this, Crashlytics())
        setHeaders()
    }

    private fun setHeaders() {
        for (interceptor in appComponent.okhttpClient().interceptors()) {
            if (interceptor is SessionAuth) {
                interceptor.setAuthHeader(sharedPreferencesRepository.getAuthHeaders())
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onTerminate() {
        sharedPreferencesRepository.isLoggedInObservable().removeObserver(isLoggedInCallback)
        super.onTerminate()
    }

    companion object {
        private lateinit var appComponent: AppComponent
        fun getAppComponent() = appComponent
    }

}