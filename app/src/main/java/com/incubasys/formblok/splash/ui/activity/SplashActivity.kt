package com.incubasys.formblok.splash.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.home.ui.HomeActivity
import com.incubasys.formblok.onboard.ui.activity.OnboardingActivity
import com.incubasys.formblok.splash.callback.SplashFragmentCallback
import com.incubasys.formblok.splash.ui.fragment.SplashFragment
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity(), SplashFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        replaceFragment(SplashFragment.newInstance(), flSplashContainer.id, animation = false)
    }

    override fun startHomeActivity(isOnboardingShown: Boolean, isLoggedIn: Boolean) {
        if (isOnboardingShown) {
            if (!isLoggedIn) {
                startActivity(AuthActivity.newIntent(this, GotoFragment.EMAIL))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        } else {
            startActivity(OnboardingActivity.newIntent(this, false))
        }
        finish()
    }

    override fun startAuthActivity() {
        startActivity(AuthActivity.newIntent(this, GotoFragment.EMAIL))
    }
}
