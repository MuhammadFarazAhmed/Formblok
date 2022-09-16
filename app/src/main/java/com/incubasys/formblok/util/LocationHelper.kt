package com.incubasys.formblok.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.*
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_DIALOG
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_SETTING
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_LOCATION_SETTINGS_ON

open class LocationHelper(private val locationUpdateCallback: LocationUpdateCallback?) {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            val location = locationResult!!.lastLocation
            locationUpdateCallback?.onLocationChanged(LatLng(location.latitude, location.longitude))
        }
    }
    private var client: SettingsClient? = null
    private var settingsRequest: LocationSettingsRequest? = null


    init {
        createLocationRequest()
    }

    fun requestPermission(activity: Activity, fragmentManager: FragmentManager) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            showMessageDialog(TYPE_ALLOW_LOCATION_PERMISSION_SETTING, fragmentManager)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CHECK_PERMISSION)
        }
    }

    private fun showMessageDialog(messageType: Int, fragmentManager: FragmentManager) {
         LocationSetupDialogFragment.newInstance(messageType).show(fragmentManager, "LocationSetupDialogFragment")
    }

    fun requestLocationSettings(context: Context) {
        val task = getLocationSettingsResponseTask(context)
        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            locationUpdateCallback?.onLocationSettingsSuccess(locationSettingsResponse.locationSettingsStates)
        }

        task.addOnFailureListener { e ->
            locationUpdateCallback?.onSettingsFailure(e)
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdate(context: Context) {
        if (mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, callback, null)
    }

    fun removeLocationUpdate() {
        mFusedLocationClient!!.removeLocationUpdates(callback)
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun startOnResultionResult(activity: Activity, e: Exception) {
        if (e is ResolvableApiException) {
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                e.startResolutionForResult(
                    activity,
                    REQUEST_CHECK_LOCATION_SETTINGS
                )
            } catch (sendEx: IntentSender.SendIntentException) {
                // Ignore the error.
            }

        }
    }

    private fun getLocationSettingsResponseTask(context: Context): Task<LocationSettingsResponse> {
        if (mLocationRequest == null) {
            createLocationRequest()
        }
        if (settingsRequest == null) {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest!!)
            builder.setAlwaysShow(true)
            settingsRequest = builder.build()
        }
        if (client == null) {
            client = LocationServices.getSettingsClient(context)
        }
        return client!!.checkLocationSettings(settingsRequest)
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun onRequestPermissionsResult(grantResults: IntArray): Boolean {
        return grantResults[0] == PackageManager.PERMISSION_GRANTED
    }


    fun onActivityResult(resultCode: Int, data: Intent?): Boolean =
        when (resultCode) {
            RESULT_OK -> {
                // All required changes were successfully made
                locationUpdateCallback?.onLocationSettingsSuccess(LocationSettingsStates.fromIntent(data!!))
                true
            }
            RESULT_CANCELED ->
                // The user was asked to change settings, but chose not to
                false
            else -> false
        }


    fun showDialogOnSettingsCancelled(fragmentManager: FragmentManager) {
        showMessageDialog(TYPE_LOCATION_SETTINGS_ON, fragmentManager)
    }

    fun showDialogOnPermissionFailed(activity: Activity, fragmentManager: FragmentManager) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageDialog(TYPE_ALLOW_LOCATION_PERMISSION_SETTING, fragmentManager)
        } else {
                showMessageDialog(TYPE_ALLOW_LOCATION_PERMISSION_DIALOG, fragmentManager)
        }
    }

    companion object {

        const val REQUEST_CHECK_LOCATION_SETTINGS = 4321
        const val REQUEST_CHECK_PERMISSION_SETTINGS = 5441
        const val REQUEST_CHECK_PERMISSION = 4321

        private const val permission = Manifest.permission.ACCESS_FINE_LOCATION
    }
}

