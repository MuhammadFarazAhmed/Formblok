package com.incubasys.formblok.projects.callback

import com.incubasys.formblok.projects.data.model.Project

interface CreateProjectFragmentCallback {

    fun onNextButtonClicked(projectOutput: Project)
}