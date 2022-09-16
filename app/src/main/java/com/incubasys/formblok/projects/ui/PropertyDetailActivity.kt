package com.incubasys.formblok.projects.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.home.ui.HomeActivity
import com.incubasys.formblok.projects.callback.NoAddressFragmentCallback
import com.incubasys.formblok.projects.callback.PropertyDetailFragmentCallback
import com.incubasys.formblok.projects.callback.PropertyListCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyMinimal
import com.incubasys.formblok.projects.ui.PropertyDetailActivity.StartActivityFrom.*
import kotlinx.android.synthetic.main.activity_project_detail.*

class PropertyDetailActivity : BaseActivity(), NoAddressFragmentCallback, PropertyListCallback,
    PropertyDetailFragmentCallback {

    override fun onSearchAddressButtonClicked() {
        setResult(Activity.RESULT_OK, Intent(this, HomeActivity::class.java).apply {
            putExtra("projectId", project?.id)
        })
        finish()
    }

    private var enum: StartActivityFrom = PROJECT_FRAGMENT
    private var comeFromSelectableFragment = false
    private var project: Project? = null

    override fun onPropertySelected(propertyMinimal: PropertyMinimal) {
        replaceFragment(
            PropertyDetailFragment.newInstance(propertyMinimal.id, comeFromSelectableFragment),
            flProjectDetailContainer.id,
            "PropertyDetail"
        )
    }

    override fun onNoAddressBackPressed() {
        onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detail)

        intent.extras?.let {
            enum = intent.getSerializableExtra("enum") as StartActivityFrom
            project = intent.getParcelableExtra("project")
        }

        project?.let {
            if (it.properties?.size!! > 0) {
                when (enum) {
                    PROJECT_FRAGMENT -> {
                        replaceFragment(
                            PropertyListFragment.newInstance(it),
                            flProjectDetailContainer.id
                        )
                    }
                    NOTIFICATION_FRAGMENT, ADDRESS_DETAIL_ACTIVITY,ADDRESS_DETAIL_ACTIVITY_2 -> {
                        //propertyMinimal = intent.getParcelableExtra("propertyMinimal")
                        comeFromSelectableFragment =
                            intent.getBooleanExtra("comeFromSelectProjectFragment", false)
                        it.properties?.get(0)?.let { it1 -> onPropertySelected(it1) }
                    }
                    CREATE_PROJECT_ACTIVITY -> {
                        replaceFragment(
                            PropertyListFragment.newInstance(it),
                            flProjectDetailContainer.id
                        )
                    }
                }
            } else {
                replaceFragment(NoAddressFragment.newInstance(), flProjectDetailContainer.id)
            }
        }
    }

    enum class StartActivityFrom {
        PROJECT_FRAGMENT, NOTIFICATION_FRAGMENT, CREATE_PROJECT_ACTIVITY, ADDRESS_DETAIL_ACTIVITY,ADDRESS_DETAIL_ACTIVITY_2
    }

    override fun onShareIconClicked() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            putExtra(Intent.EXTRA_TEXT, project?.shareUrl)
        }

        val chooser = Intent.createChooser(intent, "Share")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

    companion object {
        fun ewIntent(
            context: Context,
            project: Project
        ): Intent =
            Intent(context, PropertyDetailActivity::class.java).apply {
                putExtra("project", project)
            }
    }
}
