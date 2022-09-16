package com.incubasys.formblok.quotation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.remote.ApiStatus.*
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.projects.ui.RenovationDetailFragment
import com.incubasys.formblok.quotation.callback.*
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.activity_create_quotation.*
import javax.inject.Inject


private const val PROGRESS_INCREMENT = 20

class CreateQuotationActivity : BaseActivity(), BuildTypeFragmentCallback, RenovationFragmentCallback,
    DevelopmentTypeFragmentCallback,
    FloorsFragmentCallback, RoomsFragmentCallback, MaterialFragmentCallback {

    override fun onNextClicked(action: ACTION) {
        when (action) {
            ACTION.DEV_TYPE -> {
                replaceFragment(
                    DevelopmentTypeFragment.newInstance(),
                    flCreateQuotationContainer.id,
                    "DevelopmentTypeFragment"
                )
            }
            ACTION.RENOVATION -> {
                replaceFragment(
                    RenovationDetailFragment.newInstance(),
                    flCreateQuotationContainer.id,
                    "RenovationFragment"
                )
            }
            ACTION.FLOORS -> {
                replaceFragment(
                    FloorFragment.newInstance(),
                    flCreateQuotationContainer.id,
                    "FloorFragment"
                )
            }
            ACTION.ROOMS -> {
                replaceFragment(
                    RoomsFragment.newInstance(),
                    flCreateQuotationContainer.id,
                    "RoomFragment"
                )
            }
            ACTION.ROOMS_DETAIL -> {
                val roomDetailFragment = RoomDetailFragment.newInstance()
                roomDetailFragment.show(supportFragmentManager, roomDetailFragment.tag)
            }
            ACTION.MATERIAL -> {
                if (!viewModel.calculatedAreaExceeds.get()) {
                    replaceFragment(
                        MaterialFragment.newInstance(),
                        flCreateQuotationContainer.id,
                        "MaterialFragment"
                    )
                } else {
                    Toast
                        .makeText(this, "Your rooms have more space than your total area.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            ACTION.DONE -> {
                val data = Intent()
                data.putExtra("data", viewModel.propertyOutput)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: CreateQuotationViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(CreateQuotationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_quotation)

        this.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )

        viewModel.propertyOutput = intent.getParcelableExtra("propertyOutput")
        viewModel.originalTotalLiveableArea.value = viewModel.propertyOutput.total_liveable_area

        replaceFragment(BuildTypeFragment.newInstance(), flCreateQuotationContainer.id)

        viewModel.getBusinessRules()

        viewModel.apiResponse.observe(this, Observer {
            when (it.status) {
                LOADING -> {
                    showProgress()
                }
                SUCCESS -> {
                    dismissProgress()
                }
                EMPTY -> {
                    dismissProgress()
                    Toast.makeText(this@CreateQuotationActivity, "Cant fetch business rules", Toast.LENGTH_SHORT).show()
                }
                ERROR -> {
                }
                COMPLETED -> {
                }
            }
        })
    }

    internal fun setProgressIncrement(progress: Int) {
        pbQuotationProgressbar.progress = PROGRESS_INCREMENT + progress
    }

    enum class ACTION {
        DEV_TYPE, RENOVATION, FLOORS, ROOMS, ROOMS_DETAIL, MATERIAL, DONE
    }

}

