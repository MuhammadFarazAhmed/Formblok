package com.incubasys.formblok.onboard.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.common.extenstions.changeStatusBarColorYellow
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.onboard.callback.OnboardingCallback
import com.incubasys.formblok.onboard.ui.fragment.OnboardingFragment
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingActivity : BaseActivity(), OnboardingCallback {


    override fun onFragmentReady() {
        val fragment = supportFragmentManager.findFragmentById(R.id.flSplashContainer)
        if (fragment != null && intent != null) {
            (fragment as OnboardingFragment)
                .setShownFromSettings(
                    intent
                        .getBooleanExtra(ARG_SHOWN_FROM, false)
                )
        }
    }


    override fun onGetStartedButtonClicked(isShownFromSettings: Boolean) {
        if (!isShownFromSettings) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            //AuthActivity.newIntent(this, GotoFragment.EMAIL)
        }
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColorYellow(this)
        setContentView(R.layout.activity_onboarding)
        replaceFragment(OnboardingFragment.newInstance(), flSplashContainer.id)
    }

    companion object {
        const val ARG_SHOWN_FROM = "ARG_SHOWN_FROM"

        fun newIntent(context: Context, shownFromSettings: Boolean): Intent {
            val intent = Intent(context, OnboardingActivity::class.java)
            intent.putExtra(ARG_SHOWN_FROM, shownFromSettings)
            return intent
        }
    }


}
