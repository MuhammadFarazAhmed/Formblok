package com.incubasys.formblok.auth.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.*
import com.incubasys.formblok.auth.ui.fragment.*
import com.incubasys.formblok.auth.viewmodel.AuthViewModel
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.*
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.common.ui.MessageAction
import com.incubasys.formblok.common.ui.MessageActivity
import com.incubasys.formblok.databinding.ActivityAuthBinding
import com.incubasys.formblok.home.ui.HomeActivity
import com.incubasys.formblok.terms.ui.PrivacyPolicyActivity
import com.incubasys.formblok.util.ImagePicker
import com.incubasys.formblok.util.LocationHelper.Companion.REQUEST_CHECK_PERMISSION_SETTINGS
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

private const val PROGRESS_INCREMENT = 20

class AuthActivity : BaseActivity(), EmailFragmentCallback, PasswordFragmentCallback, ForgotPasswordCallback,
    ResetPasswordCallback, CreatePasswordFragmentCallback, UserTypeFragmentCallback, NameFragmentCallback,
    PhotoFragmentCallback, CropFragmentCallback, ImagePicker.OnPermissionCallback, DobFragmentCallback,
    GenderFragmentCallback {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ActivityAuthBinding

    val viewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.lifecycleOwner = this

        binding.vm = viewModel
        initFragment()
        onBackArrowClicked()

        viewModel.signUpOutput.observe(this, Observer {
            when (it.status) {
                ApiStatus.SUCCESS
                -> {
                    dismissProgress()
                    startActivity(
                        MessageActivity.newIntent(
                            this,
                            R.drawable.ic_tick,
                            getString(R.string.account_created),
                            getString(R.string.you_have_successfully_created_your_account),
                            getString(R.string.continue_label),
                            MessageAction.LOGIN_FROM_SIGNUP
                        )
                    )
                    finish()
                }
                ApiStatus.LOADING -> showProgress()
                ApiStatus.ERROR -> {
                    dismissProgress()
                    Toast.makeText(
                        applicationContext,
                        it.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                }
            }
        })

        viewModel.loginOutput.observe(this, Observer {
            when (it.status) {
                ApiStatus.SUCCESS
                -> {
                    dismissProgress()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
                ApiStatus.LOADING -> showProgress()
                ApiStatus.ERROR -> {
                    dismissProgress()
                    Toast.makeText(
                        applicationContext,
                        it.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                }
            }
        })

        viewModel.loginOutputWithNewPassword.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        })

        viewModel.forgotMessage.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    onEmailSent()
                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        })

        viewModel.resetMessage.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    login()
                }
                ApiStatus.ERROR -> {
                    dismissProgress()
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        })

        handleIntent()
    }

    private fun handleIntent() {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            viewModel.appLinkCode.value = appLinkData?.getQueryParameter("token") ?: ""
            viewModel.appLinkCode.observe(this, Observer {
                if (it.isNotBlank()) {
                    replaceFragment(ResetPasswordFragment.newInstance(), flAuthContainer.id)
                    viewModel.resetPasswordInput.token = it
                } else {
                    Snackbar.make(flAuthContainer, "Pin is Empty", Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent()
    }

    /**
     * Fragments start up callbacks
     */
    override fun onEmailFragmentReady() {
        pbAuthProgressbar.makeInvisible()
        ivAuthBackArrow.makeInvisible()
        ivAuthLogo.makeVisible()
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.welcome_to_formblok), getString(R.string.formblok_io))
    }

    override fun onPasswordFragmentReady() {
        ivAuthBackArrow.makeVisible()
        ivAuthLogo.makeInvisible()
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.please_enter_your_password), getString(R.string.password))
    }

    override fun onForgotPasswordFragmentReady() {
        tvAuthHeading.text =
            spannedHeadingReverse(
                this, getString(R.string.forgot_password_with_sub_heading)
                , getString(R.string.forgot_password_sub)
                , "Forgot password?"
            )
    }

    override fun onResetPasswordFragmentCallback() {
        ivAuthLogo.makeInvisible()
        tvAuthHeading.text =
            spannedHeadingReverse(
                this, getString(R.string.reset_password_with_sub_heading)
                , getString(R.string.reset_password_instructions)
                , "Reset password"
            )
    }

    override fun onCreatePasswordFragmentReady() {
        pbAuthProgressbar.makeVisible()
        pbAuthProgressbar.progress = PROGRESS_INCREMENT
        ivAuthBackArrow.makeVisible()
        ivAuthLogo.makeInvisible()
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.create_your_password), getString(R.string.password))
    }

    override fun onUserTypeFragmentReady() {

        ivAuthBackArrow.makeVisible()
        pbAuthProgressbar.progress = PROGRESS_INCREMENT + 20
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.what_type_of_user_are_you), getString(R.string.are_you))
    }

    override fun onNameFragmentReady() {
        ivAuthBackArrow.makeVisible()
        pbAuthProgressbar.progress = PROGRESS_INCREMENT + 40
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.what_is_your_name), getString(R.string.name))
    }

    override fun onPhotoFragmentReady() {
        ivAuthBackArrow.makeVisible()
        pbAuthProgressbar.progress = PROGRESS_INCREMENT + 60
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.what_do_you_look_like), getString(R.string.like))
    }

    override fun onDobFragmentReady() {
        ivAuthBackArrow.makeVisible()
        pbAuthProgressbar.makeVisible()
        pbAuthProgressbar.progress = PROGRESS_INCREMENT + 80
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.when_were_you_born), getString(R.string.born))
    }

    override fun onGenderFragmentReady() {
        ivAuthBackArrow.makeVisible()
        pbAuthProgressbar.makeVisible()
        pbAuthProgressbar.progress = PROGRESS_INCREMENT + 100
        tvAuthHeading.text =
            spannedHeading(this, getString(R.string.what_is_your_gender), getString(R.string.gender))
    }

    /**
     * Callbacks for User Type Fragment
     */
    override fun onBuyerClicked() {
        // Toast.makeText(this, "buyer", Toast.LENGTH_SHORT).show()
    }

    override fun onAgentClicked() {
        //Toast.makeText(this, "Agent", Toast.LENGTH_SHORT).show()
    }

    /**
     * Callbacks for gender type fragment
     */
    override fun onFemaleClicked() {
        //Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show()
    }

    override fun onMaleClicked() {
        //  Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show()
    }

    /**
     * Callbacks for Photo Fragment
     */
    override fun onChoosePhotoClicked() {
        viewModel.imagePicker.startActivity(this, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.REQUEST_CHECK_PERMISSION_SETTINGS) {
            onChoosePhotoClicked()
        }
        viewModel.onActivityResult(requestCode, resultCode, data)
        viewModel.imagePath?.let { CropFragment.newInstance(it) }?.let {
            addFragment(
                it,
                rlAuthContainer.id,
                "CropFragment"
            )
        }
        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.signUp()
            }
        }
        if (requestCode == 12345) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.signInWithNewPassword()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (viewModel.imagePicker.onRequestPermissionsResult(grantResults)) {
            onChoosePhotoClicked()
        } else {
            shouldShowPermissionMessage()
        }

    }

    override fun shouldShowPermissionMessage() {
        var permissionMessage: Snackbar? = null
        if (permissionMessage == null) {
            permissionMessage = Snackbar.make(
                rlAuthContainer,
                getString(R.string.allow_storage_permission_message),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.allow_permission) {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            this.packageName, null
                        )
                    )
                    startActivityForResult(intent, REQUEST_CHECK_PERMISSION_SETTINGS)
                }
        }
        permissionMessage.show()
    }

    /**
     * Callbacks for Crop Fragment
     */
    override fun onCancelButtonClicked() {
        supportFragmentManager.popBackStack()
    }

    override fun onConfirmButtonClicked() {
        supportFragmentManager.popBackStack()
    }

    /**
     * Generic callback for all Auth fragments Next button
     */
    override fun onNextButtonClicked(gotoFragment: GotoFragment) {
        hideKeyboard(flAuthContainer)
        when (gotoFragment) {
            GotoFragment.EMAIL -> replaceFragment(
                EmailFragment.newInstance(),
                flAuthContainer.id
            )
            GotoFragment.PASSWORD -> replaceFragment(
                PasswordFragment.newInstance(),
                flAuthContainer.id,
                "PasswordFragment"
            )

            GotoFragment.FORGOT -> replaceFragment(
                ForgotFragment.newInstance(),
                flAuthContainer.id,
                "ForgotPassword"
            )

            GotoFragment.RESET -> replaceFragment(
                ResetPasswordFragment.newInstance(),
                flAuthContainer.id,
                "ResetPasswordFragment"
            )

            GotoFragment.CREATE_PASSWORD -> replaceFragment(
                CreatePasswordFragment.newInstance(),
                flAuthContainer.id,
                "CreatePasswordFragment"
            )

            GotoFragment.USER_TYPE -> replaceFragment(
                UserTypeFragment.newInstance(),
                flAuthContainer.id,
                "UserTypeFragment"
            )

            GotoFragment.NAME -> replaceFragment(
                NameFragment.newInstance(),
                flAuthContainer.id,
                "NameFragment"
            )

            GotoFragment.PHOTO -> replaceFragment(
                PhotoFragment.newInstance(),
                flAuthContainer.id,
                "PhotoFragment"
            )

            GotoFragment.CROP -> addFragment(
                CropFragment.newInstance(viewModel.imagePath!!),
                rlAuthContainer.id
            )

            GotoFragment.DATE -> replaceFragment(
                DobFragment.newInstance(),
                flAuthContainer.id,
                "DobFragment"
            )

            GotoFragment.GENDER -> replaceFragment(
                GenderFragment.newInstance(),
                flAuthContainer.id,
                "GenderFragment"
            )

            GotoFragment.LOGIN -> viewModel.signIn()

            GotoFragment.ACCOUNT_CREATED -> startActivity(Intent(this, PrivacyPolicyActivity::class.java))

            GotoFragment.EMAIL_SENT -> onEmailSent()

            GotoFragment.RESET_PASSWORD_SUCCESS -> login()

            GotoFragment.PRIVACY_POLICY -> openBottomSheet()

        }
    }

    private fun openBottomSheet() {
        val intent = Intent(this, PrivacyPolicyActivity::class.java)
        intent.putExtra("wherefrom", PrivacyPolicyActivity.FROM.AUTH)
        startActivityForResult(intent, 1234)
    }

    private fun initFragment() {
        val gotoFragment = intent.getSerializableExtra("type")
        if (gotoFragment == null) {
            replaceFragment(EmailFragment.newInstance(), flAuthContainer.id)
        }
        when (gotoFragment) {
            GotoFragment.EMAIL -> replaceFragment(EmailFragment.newInstance(), flAuthContainer.id)
            GotoFragment.FORGOT -> replaceFragment(ForgotFragment.newInstance(), flAuthContainer.id)
            GotoFragment.RESET -> replaceFragment(ResetPasswordFragment.newInstance(), flAuthContainer.id)
            GotoFragment.DATE -> replaceFragment(DobFragment.newInstance(), flAuthContainer.id)
            GotoFragment.GENDER -> replaceFragment(GenderFragment.newInstance(), flAuthContainer.id)
        }
    }

    private fun onBackArrowClicked() {
        ivAuthBackArrow.setOnClickListener {
            if (pbAuthProgressbar.progress >= PROGRESS_INCREMENT) {
                val progress = pbAuthProgressbar.max - PROGRESS_INCREMENT
                pbAuthProgressbar.progress = progress
                onBackPressed()
            } else {
                onBackPressed()
            }
        }
    }

    private fun onEmailSent() {
        startActivity(
            MessageActivity.newIntent(
                this,
                R.drawable.email_sent,
                getString(R.string.email_sent),
                getString(R.string.reset_password_instructions),
                getString(R.string.open_mail_app),
                MessageAction.EMAIL
            )
        )
        replaceFragment(
            ResetPasswordFragment.newInstance(),
            flAuthContainer.id
            , "ResetPasswordFragment"
        )
    }

    private fun login() {
        startActivityForResult(
            MessageActivity.newIntent(
                this,
                R.drawable.ic_tick,
                getString(R.string.password_reset),
                getString(R.string.you_have_successfully_reset_your_password),
                getString(R.string.login),
                MessageAction.LOGIN
            ), 12345
        )
    }

    companion object {
        fun newIntent(context: Context, type: GotoFragment): Intent =
            Intent(context, AuthActivity::class.java).apply {
                putExtra("type", type)
            }
    }
}


enum class GotoFragment {
    EMAIL,
    PASSWORD,
    FORGOT,
    RESET,
    EMAIL_SENT,
    RESET_PASSWORD_SUCCESS,
    CREATE_PASSWORD,
    USER_TYPE,
    NAME,
    PHOTO,
    CROP,
    DATE,
    GENDER,
    ACCOUNT_CREATED,
    PRIVACY_POLICY,
    LOGIN
}
