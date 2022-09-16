package com.incubasys.formblok.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.incubasys.formblok.BuildConfig
import com.incubasys.formblok.common.data.remote.api.UserApi
import com.incubasys.formblok.common.data.remote.client.*
import com.incubasys.formblok.di.activitylevel.RepositoryModule
import com.incubasys.formblok.di.scope.AppScope
import com.incubasys.formblok.util.Constants
import com.incubasys.formblok.util.ParseErrors
import com.incubasys.formblok.util.PreferencesHelper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@Module(includes = [RepositoryModule::class])
class AppModule {


    @Provides
    @AppScope
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonCustomConverterFactory.create(gson))
        .client(client).build()

    @Provides
    @AppScope
    fun okHttpClient(
       // loggingInterceptor: HttpLoggingInterceptor,
        keyAuth: ApiKeyAuth,
        sessionAuth: SessionAuth
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
           // .addInterceptor(loggingInterceptor)
            .addInterceptor(keyAuth)
            .addInterceptor(sessionAuth)
            .build()

    @Provides
    @AppScope
    fun provideGson(): Gson = GsonBuilder()
        .setDateFormat(Constants.DATE_FORMAT_PATTERN)
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .setPrettyPrinting()
        .create()

    @Provides
    @AppScope
    fun apiKeyAuth(): ApiKeyAuth {
        val apiKeyAuth = ApiKeyAuth("header", "api")
        apiKeyAuth.apiKey = BuildConfig.API_KEY
        return apiKeyAuth
    }

    /*@Provides
    @AppScope
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }*/

    @Provides
    fun context(application: Application): Context = application.applicationContext

    @Provides
    fun preferencesHelper(sharedPreferences: SharedPreferences): PreferencesHelper =
        PreferencesHelper(sharedPreferences)

    @Provides
    @AppScope
    fun parseErrors(gson: Gson): ParseErrors = ParseErrors(gson)

    @Provides
    @AppScope
    fun userApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)


    @Provides
    @AppScope
    fun sharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)


}
