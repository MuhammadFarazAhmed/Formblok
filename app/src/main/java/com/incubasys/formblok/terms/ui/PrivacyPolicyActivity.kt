package com.incubasys.formblok.terms.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.addFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.databinding.ActivityPrivacyPolicyBinding
import com.incubasys.formblok.terms.callback.LegalFragmentCallback
import com.incubasys.formblok.terms.callback.PrivacyPolicyFragmentCallback
import com.incubasys.formblok.terms.viewmodel.PrivacyViewModel
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import javax.inject.Inject

class PrivacyPolicyActivity : BaseActivity(), PrivacyPolicyFragmentCallback, LegalFragmentCallback {

    private lateinit var whereFrom: FROM
    private var goto: Int? = -1
    private var privacyPolicyDialogFragment = PrivacyPolicyDialogFragment.newInstance()

    companion object {
        fun newIntent(context: Context, whereFrom: FROM, gotoFragment: Int): Intent =
            Intent(context, PrivacyPolicyActivity::class.java).apply {
                putExtra("wherefrom", whereFrom)
                putExtra("goto", gotoFragment)
            }
    }

    private lateinit var binding: ActivityPrivacyPolicyBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: PrivacyViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(PrivacyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
        viewModel.getSupportContent()
        whereFrom = intent.getSerializableExtra("wherefrom") as FROM
        goto = intent.getIntExtra("goto", -1)
        privacyPolicyDialogFragment = PrivacyPolicyDialogFragment()
        initFragment()
    }

    private fun initFragment() {
        if (whereFrom == FROM.AUTH) {
            /* replaceFragment(PrivacyPolicyFragment.newInstance(), flPrivacyPolicyContainer.id, animation = false)
             initBottomSheet()*/
            privacyPolicyDialogFragment.show(supportFragmentManager, PrivacyPolicyDialogFragment().tag)
        } else if (whereFrom == FROM.SETTINGS) {
            when (goto) {
                1 -> {
                    onPrivacyPolicyClicked(1)
                }
                2 -> {
                    onTermsConditionClicked(2)
                }
            }
        }
    }

    private fun initBottomSheet() {
        val sheetBehavior = BottomSheetBehavior.from((flPrivacyPolicyContainer))
        sheetBehavior.isHideable = true
        sheetBehavior.skipCollapsed = false
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    finish()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    override fun onAcceptButtonClicked() {
        setResult(
            Activity.RESULT_OK
        )
        finish()
    }

    override fun onPrivacyPolicyClicked(type: Int) {
        if (privacyPolicyDialogFragment.isVisible) {
            privacyPolicyDialogFragment.dialog?.hide()
        }
        addFragment(LegalFragment.newInstance(1), clPrivacyPolicyRootContainer.id, "LegalFragment")
    }

    override fun onTermsConditionClicked(type: Int) {
        if (privacyPolicyDialogFragment.isVisible) {
            privacyPolicyDialogFragment.dialog?.hide()
        }
        addFragment(LegalFragment.newInstance(2), clPrivacyPolicyRootContainer.id, "LegalFragment")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (whereFrom == FROM.AUTH) {
            supportFragmentManager.popBackStack()
            privacyPolicyDialogFragment.dialog?.show()
        } else if (whereFrom == FROM.SETTINGS) {
            finish()
        }
    }

    override fun onBackButtonClicked() {
        if (whereFrom == FROM.AUTH) {
            supportFragmentManager.popBackStack()
            privacyPolicyDialogFragment.dialog?.show()
        } else if (whereFrom == FROM.SETTINGS) {
            finish()
        }
    }

    enum class FROM {
        AUTH,
        SETTINGS
    }
}
