package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.ProjectsRepository
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.util.ParseErrors
import javax.inject.Inject

class PropertyListViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val projectsRepository: ProjectsRepository
) : BaseViewModel(application, parseErrors) {


    var project = MutableLiveData<Project>()


}