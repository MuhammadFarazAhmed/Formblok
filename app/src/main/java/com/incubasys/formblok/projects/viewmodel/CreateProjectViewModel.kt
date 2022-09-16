package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.ProjectInput
import com.incubasys.formblok.projects.data.model.ProjectOutput
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateProjectViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) : BaseViewModel(application, parseErrors) {


    val enableNextButton = ObservableField<Boolean>(false)
    var projectNameError = ObservableField<String>("")

    var projectInput = ProjectInput()

    var property = MutableLiveData<PropertyInput>()

    var projectOutput = MutableLiveData<ApiResponse<Project>>()

    fun validateProjectName() {
        if (projectInput.name.isEmpty()) {
            enableNextButton.set(false)
            projectNameError.set("Name cant be blank")
        } else {
            enableNextButton.set(true)
            projectNameError.set(null)
        }
    }

    fun createProject() = projectsRepository.createAProject(projectInput)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .doOnSubscribe { projectOutput.value = ApiResponse(ApiStatus.LOADING) }
        .subscribeBy(onNext = {
            projectOutput.value = ApiResponse(ApiStatus.SUCCESS, it)
        }, onError = {
            projectOutput.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
        })

}
