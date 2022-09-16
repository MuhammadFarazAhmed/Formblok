package com.incubasys.formblok.projects.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.common.ui.MessageAction
import com.incubasys.formblok.common.ui.MessageActivity
import com.incubasys.formblok.home.ui.HomeActivity
import com.incubasys.formblok.projects.callback.CreateProjectFragmentCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyInput

class CreateProjectActivity : BaseActivity(), CreateProjectFragmentCallback {

    private var propertyInput: PropertyInput? = null
    private var projectOutput: Project? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_project_activity)
        if (intent != null) {
            propertyInput = intent.getParcelableExtra("property")
        }
        replaceFragment(CreateProjectFragment.newInstance(propertyInput), R.id.container)

    }

    override fun onNextButtonClicked(projectOutput: Project) {
        this.projectOutput = projectOutput
        /* startActivityForResult(
             MessageActivity.newIntent(
                 this,
                 R.drawable.ic_tick,
                 getString(R.string.project_created),
                 getString(R.string.you_have_successfully_created_your_project),
                 getString(R.string.continue_label),
                 MessageAction.PROJECT_CREATED
             ), 7869
         )*/
        setResult(Activity.RESULT_OK, Intent(this, HomeActivity::class.java).apply {
            putExtra("project", projectOutput)
        })
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7869) {
          /*  if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, PropertyDetailActivity::class.java)
                intent.putExtra(
                    "enum",
                    PropertyDetailActivity.StartActivityFrom.CREATE_PROJECT_ACTIVITY
                )
                intent.putExtra("project", projectOutput)
                startActivity(intent)
                finish()
            }*/
        }
    }

}
