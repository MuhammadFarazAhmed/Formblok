package com.incubasys.formblok.profile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.CropFragmentCallback
import com.incubasys.formblok.auth.callback.EditPhotoFragmentCallback
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.addFragment
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.common.ui.MessageAction
import com.incubasys.formblok.common.ui.MessageActivity
import com.incubasys.formblok.profile.callback.*
import com.incubasys.formblok.profile.viewmodel.EditProfileViewModel
import com.incubasys.formblok.util.ImagePicker
import com.incubasys.formblok.util.LocationHelper
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.edit_profile_activity.*
import javax.inject.Inject


class EditProfileActivity : BaseActivity(), EditProfileFragmentCallback, ImagePicker.OnPermissionCallback,
    CropFragmentCallback, EditPhotoFragmentCallback, EditEmailFragmentCallback, EditNameFragmentCallback,
    EditGenderFragmentCallback, EditDobFragmentCallback {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel: EditProfileViewModel by lazy {
        ViewModelProviders.of(this, factory).get(EditProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_activity)
        this.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        replaceFragment(EditProfileFragment.newInstance(), rlEditProfileContainer.id, animation = false)

        viewModel.userObserver.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> showProgress()
                ApiStatus.SUCCESS -> {
                    dismissProgress()
                    setResult(Activity.RESULT_OK)
                    startActivity(
                        MessageActivity.newIntent(
                            this,
                            R.drawable.ic_tick,
                            getString(R.string.profile_updated),
                            getString(R.string.you_have_successfully_updated_your_profile),
                            getString(R.string.continue_label),
                            MessageAction.PASSWORD_CHANGE
                        )
                    )
                    finish()
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
        Snackbar.make(rlEditProfileContainer, message.message, Snackbar.LENGTH_INDEFINITE).setAction(
            R.string.retry
        ) { viewModel.updateProfile() }.show()
    }

    override fun onChangePhotoClicked() {
        viewModel.imagePicker.startActivity(this, this)
    }

    override fun onNameItemClicked() {
        addFragment(
            EditNameFragment.newInstance(),
            rlEditProfileContainer.id,
            "EditNameFragment"
        )
    }

    override fun onEmailItemClicked() {
        addFragment(
            EditEmailFragment.newInstance(),
            rlEditProfileContainer.id,
            "EditEmailFragment"
        )
    }

    override fun onGenderItemClicked() {
        addFragment(
            EditGenderFragment.newInstance(),
            rlEditProfileContainer.id,
            "EditGenderFragment"
        )
    }

    override fun onDobItemClicked() {
        addFragment(
            EditDobFragment.newInstance(),
            rlEditProfileContainer.id,
            "EditDobFragment"
        )
    }

    override fun onSaveButtonClicked() {
        viewModel.updateProfile()
    }

    override fun onBackPress() {
        onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.REQUEST_CHECK_PERMISSION_SETTINGS) {
            onChangePhotoClicked()
        }
        viewModel.onActivityResult(requestCode, resultCode, data)
        viewModel.imagePath?.let {
            EditCropFragment.newInstance(it)
        }?.let {
            addFragment(
                it,
                rlEditProfileContainer.id,
                "EditCropFragment"
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (viewModel.imagePicker.onRequestPermissionsResult(grantResults)) {
            onChangePhotoClicked()
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
                    startActivityForResult(intent, LocationHelper.REQUEST_CHECK_PERMISSION_SETTINGS)
                }
        }
        permissionMessage.show()
    }

    override fun onConfirmButtonClicked() {
        addFragment(EditPhotoFragment.newInstance(), rlEditProfileContainer.id, "EditPhotoFragment")
    }

    override fun onFragmentBackPressed() {
        hideKeyboard(rlEditProfileContainer)
        for (i in supportFragmentManager.backStackEntryCount - 1 downTo 0) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onNextIconClicked() {
        hideKeyboard(rlEditProfileContainer)
        for (i in supportFragmentManager.backStackEntryCount - 1 downTo 0) {
            supportFragmentManager.popBackStack()
        }
        EditProfileFragment.notifyAdaptor()
    }

    override fun onEditPhotoChangePhotoClicked() {
        onChangePhotoClicked()
    }

    override fun onCancelButtonClicked() {
        onBackPressed()
    }

    override fun onEditPhotoBackPress() {
        onBackPressed()
    }

}
