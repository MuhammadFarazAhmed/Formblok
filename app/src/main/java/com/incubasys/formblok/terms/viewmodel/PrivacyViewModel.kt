package com.incubasys.formblok.terms.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.terms.data.PrivacyPolicyRepository
import com.incubasys.formblok.common.data.model.SupportContentOutput
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PrivacyViewModel @Inject constructor(
    private val privacyPolicyRepository: PrivacyPolicyRepository,
    application: Application
    , private val parseErrors: ParseErrors
) : BaseViewModel(application, parseErrors) {

    var supportContent = MutableLiveData<ApiResponse<SupportContentOutput>>()

    fun getSupportContent() = compositeDisposable.add(privacyPolicyRepository.getSupportContent()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {

        }.subscribeBy(onNext = {
            supportContent.value = ApiResponse(ApiStatus.SUCCESS, it)
        }, onError = {
            supportContent.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
        })
    )

}