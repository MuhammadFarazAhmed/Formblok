package com.incubasys.formblok.settings.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.addFragment
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.common.ui.MessageAction
import com.incubasys.formblok.common.ui.MessageActivity
import com.incubasys.formblok.onboard.ui.activity.OnboardingActivity
import com.incubasys.formblok.profile.callback.SettingsFragmentCallback
import com.incubasys.formblok.settings.callback.ChangeNewPasswordFragmentCallback
import com.incubasys.formblok.settings.callback.ChangeOldPasswordFragmentCallback
import com.incubasys.formblok.settings.callback.LogOutFragmentCallback
import com.incubasys.formblok.settings.viewmodel.SettingsViewModel
import com.incubasys.formblok.terms.ui.PrivacyPolicyActivity
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_fragment.*
import javax.inject.Inject

class SettingsActivity : BaseActivity(), SettingsFragmentCallback, LogOutFragmentCallback,
    ChangeOldPasswordFragmentCallback, ChangeNewPasswordFragmentCallback {

    private var logOutFragment: LogOutFragment? = null
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel: SettingsViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        this.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        replaceFragment(SettingsFragment.newInstance(), flSettingsContainer.id)
        logOutFragment = LogOutFragment.newInstance()

        viewModel.logoutLiveData.observe(this, Observer {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    finishAffinity()
                    startActivity(AuthActivity.newIntent(this, GotoFragment.EMAIL))
                }
                ApiStatus.LOADING -> showProgress()
                ApiStatus.ERROR -> {
                    dismissProgress()
                    showError(it.error!!)
                }
                else -> {

                }
            }
        })

        viewModel.changePasswordMessage.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    hideKeyboard(clSettingsFragmentContainer)
                    startActivity(
                        MessageActivity.newIntent(
                            this,
                            R.drawable.ic_tick,
                            getString(R.string.password_created),
                            getString(R.string.you_have_successfully_change_your_password),
                            getString(R.string.continue_label),
                            MessageAction.PASSWORD_CHANGE
                        )
                    )
                    for (i in supportFragmentManager.backStackEntryCount - 1 downTo 0) {
                        supportFragmentManager.popBackStack()
                    }
                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                    it.error?.let { it1 -> showError(it1) }
                }
                else -> {

                }
            }
        })
    }

    private fun showError(message: Message) {
        Snackbar.make(clSettingsFragmentContainer, message.message, Snackbar.LENGTH_INDEFINITE).setAction(
            R.string.retry
        ) { viewModel.logOut() }.show()
    }

    override fun onChangePasswordClicked() {
        addFragment(ChangeOldPasswordFragment.newInstance(), clSettingsFragmentContainer.id, "ChangeOldPassword")
    }

    override fun onPrivacyPolicyClicked() {
        startActivity(PrivacyPolicyActivity.newIntent(this, PrivacyPolicyActivity.FROM.SETTINGS, 1))
    }

    override fun onTermsAndConditionClicked() {
        startActivity(PrivacyPolicyActivity.newIntent(this, PrivacyPolicyActivity.FROM.SETTINGS, 2))
    }

    override fun onTutorialClicked() {
        //OnboardingActivity.newIntent(this, true) not working
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.putExtra("ARG_SHOWN_FROM", true)
        startActivity(intent)
    }

    override fun onContactAdmin() {
        val intent = ShareCompat.IntentBuilder.from(this)
            .setType("message/rfc822")
            .addEmailTo("info@formblok.io")
            .setSubject("Mail From App")
            .setText("")
            .setChooserTitle("Choose email from")
            .createChooserIntent()
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onbackPress() {
        onBackPressed()
    }

    override fun onLogout() {
        logOutFragment?.show(supportFragmentManager, logOutFragment?.tag)
    }

    override fun onCancelButtonClicked() {
        logOutFragment?.dismiss()
    }

    override fun onLogoutButtonClicked() {
        viewModel.logOut()
    }

    override fun onChangeOldPasswordNextClicked() {
        addFragment(ChangeNewPasswordFragment.newInstance(), clSettingsFragmentContainer.id, "ChangeNewPassword")
    }

    override fun onChangeNewPasswordNextClicked() {
        viewModel.changePassword()
    }

    override fun onFragmentBackPressed() {
        hideKeyboard(clSettingsFragmentContainer)
        onBackPressed()
    }
}
