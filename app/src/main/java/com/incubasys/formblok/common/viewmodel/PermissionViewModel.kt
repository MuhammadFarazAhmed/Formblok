package com.incubasys.formblok.common.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.incubasys.formblok.FormBlokApplication
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.*
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_DIALOG
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_SETTING
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_LOCATION_SETTINGS_ON
import com.incubasys.formblok.util.ParseErrors
import javax.inject.Inject

class PermissionViewModel @Inject
constructor(application: Application) :
    AndroidViewModel(application) {

    val message = ObservableField<String>()
    val positiveButtonText = ObservableField<String>()
    private var messageType = 0

    fun setMessageType(messageType: Int) {
        this.messageType = messageType
        updateText(messageType)

    }

    private fun updateText(messageType: Int) {
        when (messageType) {
            TYPE_ALLOW_LOCATION_PERMISSION_DIALOG, TYPE_ALLOW_LOCATION_PERMISSION_SETTING -> {
                message.set(getApplication<FormBlokApplication>().resources.getString(R.string._please_allow_location_permission))
                positiveButtonText.set(getApplication<FormBlokApplication>().resources.getString(R.string.allow_permission))
            }
            TYPE_LOCATION_SETTINGS_ON -> {
                message.set(getApplication<FormBlokApplication>().resources.getString(R.string._please_turn_on_your_location_from_settings))
                positiveButtonText.set(getApplication<FormBlokApplication>().resources.getString(R.string.turn_on_location))
            }
        }
    }
}
