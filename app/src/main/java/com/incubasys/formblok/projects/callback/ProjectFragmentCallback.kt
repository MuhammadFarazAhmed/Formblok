package com.incubasys.formblok.projects.callback

import com.incubasys.formblok.projects.data.model.Project

interface ProjectFragmentCallback {

    fun onAddProjectButtonClicked()

    fun onProjectSelected(project: Project)
}