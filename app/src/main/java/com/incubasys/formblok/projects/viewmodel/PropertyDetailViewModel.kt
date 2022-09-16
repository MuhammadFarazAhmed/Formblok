package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PropertyDetailViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) : BaseViewModel(application, parseErrors) {

    var propertyId: Int? = null
    var comeFromSelectProjectFragment = false
    var propertyOutput = MutableLiveData<ApiResponse<PropertyOutput>>()

    fun getPropertyDetailById() = propertyId?.let {
        projectsRepository.getPropertyDetail(it)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                propertyOutput.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = { propertyOutput ->
                this.propertyOutput.value = ApiResponse(ApiStatus.SUCCESS, propertyOutput)
            }, onError = { throwable ->
                propertyOutput.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(throwable))
            })
    }
}