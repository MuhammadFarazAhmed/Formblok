package com.incubasys.formblok.quotation.di

import com.incubasys.formblok.di.scope.ActivityScope
import com.incubasys.formblok.quotation.data.QuotationRepository
import com.incubasys.formblok.quotation.data.api.QuotationApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class QuotationModule {

    @Provides
    @ActivityScope
    fun provideQuotationApi(retrofit: Retrofit) = retrofit.create(QuotationApi::class.java)

    @Provides
    @ActivityScope
    fun provideQuotationRepository(quotationApi: QuotationApi) = QuotationRepository(quotationApi)
}