package com.incubasys.formblok.common.data.pagination

import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.common.data.model.ProjectPage
import com.incubasys.formblok.projects.data.api.ProjectApi
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.*

class ProjectDataSource(override var parseErrors: ParseErrors, var projectApi: ProjectApi) :
    BaseDataSource<Project, ProjectApi>(parseErrors, projectApi) {

    private var query: String? = null

    fun setParams(query: String?) {
        this.query = query
    }

    override fun getObservable(params: PageInfo): Observable<List<Project>> {
        return api.getProjectList(params.page, params.timestamp,query)
            .flatMap(Function<ProjectPage, ObservableSource<List<Project>>> { t->
                currentPageInfo = t.pageInfo
                if (t.projects != null && t.projects!!.isNotEmpty()) {
                    return@Function Observable.fromIterable(t.projects)
                        .toList().toObservable()
                } else {
                    val items = ArrayList<Project>()
                    return@Function Observable.just<List<Project>>(items)
                }
            })
    }
}
