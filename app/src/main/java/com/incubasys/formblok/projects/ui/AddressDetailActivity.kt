package com.incubasys.formblok.projects.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.addFragment
import com.incubasys.formblok.common.extenstions.changeStatusBarColorWhite
import com.incubasys.formblok.common.extenstions.clearLightStatusBar
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.projects.callback.AddressDetailFragmentCallback
import com.incubasys.formblok.projects.callback.SelectProjectFragmentCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.data.model.PropertyMinimal
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.quotation.ui.CreateQuotationActivity
import kotlinx.android.synthetic.main.activity_address_detail.*

class AddressDetailActivity : BaseActivity(), AddressDetailFragmentCallback, SelectProjectFragmentCallback {

    companion object {
        const val GET_QUOTE_REQUEST_CODE = 7860
    }

    private var addressId: Int = -1
    private var projectId: Int = -1
    private var latLng= LatLng(0.0,0.0)
    private var propertyOutput: PropertyOutput? = null
    private var propertyInput = PropertyInput()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_detail)
        addressId = intent.getIntExtra("addressId", -1)
        projectId = intent.getIntExtra("projectId", -1)
        latLng = intent.getParcelableExtra("latlng")
        replaceFragment(AddressDetailFragment.newInstance(projectId,addressId,latLng), flAddressDetailContainer.id)

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.flAddressDetailContainer)
            if (fragment is SelectProjectFragment) {
                changeStatusBarColorWhite(this)
            } else {
                clearLightStatusBar()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_QUOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            propertyOutput = data?.getParcelableExtra("data")
            val fragment = supportFragmentManager.findFragmentById(flAddressDetailContainer.id)
            if (fragment is AddressDetailFragment) {
                if (propertyOutput != null) {
                    fragment.updatePropertyOutput(propertyOutput!!)
                    fragment.addQuoteFragment()
                }
            }
        }
    }

    override fun openQuotationActivity(propertyOutput: PropertyOutput?) {
        val intent = Intent(this@AddressDetailActivity, CreateQuotationActivity::class.java)
        intent.putExtra("propertyOutput", propertyOutput)
        startActivityForResult(intent, GET_QUOTE_REQUEST_CODE)
    }


    override fun openAddToProjectActivity() {
        addFragment(
            SelectProjectFragment.newInstance(propertyOutput?.toPropertyInput(propertyInput)),
            flAddressDetailContainer.id,
            "SelectProjectFragment"
        )
    }

    override fun openPropertyDetailDirect(project: Project, propertyMinimal: PropertyMinimal?) {
        val intent = Intent(this, PropertyDetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("enum", PropertyDetailActivity.StartActivityFrom.ADDRESS_DETAIL_ACTIVITY_2)
        intent.putExtra("project", project)
        intent.putExtra("propertyMinimal", propertyMinimal)
        intent.putExtra("comeFromSelectProjectFragment", true)
        startActivity(intent)
        finish()
    }

    override fun openPorpertyDetailActivity(project: Project, propertyMinimal: PropertyMinimal?) {
        val intent = Intent(this, PropertyDetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("enum", PropertyDetailActivity.StartActivityFrom.ADDRESS_DETAIL_ACTIVITY)
        intent.putExtra("project", project)
        intent.putExtra("propertyMinimal", propertyMinimal)
        intent.putExtra("comeFromSelectProjectFragment", true)
        startActivity(intent)
        finish()
    }
}
