package com.incubasys.formblok.terms.di

import com.incubasys.formblok.terms.data.PrivacyPolicyRepository
import com.incubasys.formblok.terms.data.api.PrivacyApi
import com.incubasys.formblok.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class PrivacyModule{
    @Provides
    @ActivityScope
     fun privacyApi(retrofit: Retrofit): PrivacyApi {
        return retrofit.create(PrivacyApi::class.java)
    }

    @Provides
    @ActivityScope
    fun privacyRepository(privacyApi: PrivacyApi): PrivacyPolicyRepository {
        return PrivacyPolicyRepository(privacyApi)
    }
}