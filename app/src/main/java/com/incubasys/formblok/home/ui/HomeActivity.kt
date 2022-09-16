package com.incubasys.formblok.home.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.BaseActivity
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_DIALOG
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_ALLOW_LOCATION_PERMISSION_SETTING
import com.incubasys.formblok.common.ui.LocationSetupDialogFragment.Companion.TYPE_LOCATION_SETTINGS_ON
import com.incubasys.formblok.common.ui.MessageAction
import com.incubasys.formblok.common.ui.MessageActivity
import com.incubasys.formblok.explore.callback.ExploreFragmentCallback
import com.incubasys.formblok.explore.ui.ExploreFragment
import com.incubasys.formblok.notification.callback.NotificationFragmentCallback
import com.incubasys.formblok.notification.data.model.NotificationInput
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.notification.ui.NotificationFragment
import com.incubasys.formblok.profile.callback.ProfileFragmentCallback
import com.incubasys.formblok.profile.ui.EditProfileActivity
import com.incubasys.formblok.profile.ui.ProfileFragment
import com.incubasys.formblok.projects.callback.ProjectFragmentCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyMinimal
import com.incubasys.formblok.projects.ui.AddressDetailActivity
import com.incubasys.formblok.projects.ui.CreateProjectActivity
import com.incubasys.formblok.projects.ui.ProjectFragment
import com.incubasys.formblok.projects.ui.PropertyDetailActivity
import com.incubasys.formblok.settings.ui.SettingsActivity
import com.incubasys.formblok.util.Constants.OPEN_EDIT_PROFILE_ACTIVITY
import com.incubasys.formblok.util.LocationHelper
import com.incubasys.formblok.util.LocationHelper.Companion.REQUEST_CHECK_LOCATION_SETTINGS
import com.incubasys.formblok.util.LocationHelper.Companion.REQUEST_CHECK_PERMISSION
import com.incubasys.formblok.util.LocationHelper.Companion.REQUEST_CHECK_PERMISSION_SETTINGS
import com.incubasys.formblok.util.LocationUpdateCallback
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), ProfileFragmentCallback, ProjectFragmentCallback,
    NotificationFragmentCallback,
    ExploreFragmentCallback, LocationSetupDialogFragment.Listener,
    LocationUpdateCallback {

    private val notificationInput = NotificationInput()

    override val markAllAsUnReadLabelClicked: () -> Unit
        get() = {

        }

    override fun onNotificationClicked(notificationOutput: NotificationOutput) {
        when (notificationOutput.notificationType) {
            0 -> { //Address
                val intent = Intent(this, AddressDetailActivity::class.java)
                intent.putExtra("addressId", notificationOutput.resourceID)
                intent.putExtra("latlng", LatLng(0.0, 0.0))
                startActivity(intent)
            }
            1 -> { // Property
                val intent = Intent(this, PropertyDetailActivity::class.java)
                intent.putExtra(
                    "enum",
                    PropertyDetailActivity.StartActivityFrom.NOTIFICATION_FRAGMENT
                )
                val properties = ArrayList<PropertyMinimal>()
                properties.add(PropertyMinimal(id = notificationOutput.resourceID))
                intent.putExtra("project", Project(properties = properties))
                //intent.putExtra("propertyMinimal", PropertyMinimal(id = notificationOutput.id))
                intent.putExtra("comeFromSelectProjectFragment", true)
                startActivity(intent)
            }
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_project -> {
                    replaceFragment(
                        ProjectFragment.newInstance(),
                        rlHomeFragmentContainer.id,
                        animationType = "fade"
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    replaceFragment(
                        ExploreFragment.newInstance(projectId),
                        rlHomeFragmentContainer.id,
                        animationType = "fade"
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    replaceFragment(
                        NotificationFragment.newInstance(notificationInput),
                        rlHomeFragmentContainer.id,
                        animationType = "fade"
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    replaceFragment(
                        ProfileFragment.newInstance(),
                        rlHomeFragmentContainer.id,
                        animationType = "fade"
                    )
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private lateinit var helper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        helper = LocationHelper(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (!handleIntent()) {
            navigation.selectedItemId = R.id.navigation_search
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent()
    }

    private fun handleIntent(): Boolean {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data

        return if (Intent.ACTION_VIEW == appLinkAction) {
            with(notificationInput) {
                notificationType = appLinkData?.getQueryParameter("notification_type")?.toInt()
                resourceID = appLinkData?.getQueryParameter("resource_id")?.toInt() ?: -1
                userName = appLinkData?.getQueryParameter("user_name") ?: ""
                comeFromAppLink = true
            }
            navigation.selectedItemId = R.id.navigation_notifications
            true
        } else {
            false
        }
    }

    override fun onProjectSelected(project: Project) {
        val intent = Intent(this, PropertyDetailActivity::class.java)
        intent.putExtra("enum", PropertyDetailActivity.StartActivityFrom.PROJECT_FRAGMENT)
        intent.putExtra("project", project)
        startActivityForResult(intent, 9898)
    }

    override fun onEditProfileTextClicked() {
        startActivityForResult(
            Intent(this, EditProfileActivity::class.java),
            OPEN_EDIT_PROFILE_ACTIVITY
        )
    }

    override fun onSettingsImageClicked() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onPermissionDialogButtonClicked(messageType: Int) {
        when (messageType) {
            TYPE_ALLOW_LOCATION_PERMISSION_DIALOG -> helper.requestPermission(
                this,
                supportFragmentManager
            )
            TYPE_ALLOW_LOCATION_PERMISSION_SETTING -> {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivityForResult(intent, REQUEST_CHECK_PERMISSION_SETTINGS)
            }
            TYPE_LOCATION_SETTINGS_ON -> helper.requestLocationSettings(this)
        }
    }

    override fun requestLocation() {
        if (helper.isLocationPermissionGranted(this)) {
            helper.requestLocationSettings(this)
        } else {
            helper.requestPermission(this, supportFragmentManager)
        }
    }

    override fun onAddressClicked(projectId: Int?, addressId: Int, latLng: LatLng) {
        val intent = Intent(this, AddressDetailActivity::class.java)
        intent.putExtra("addressId", addressId)
        intent.putExtra("projectId", projectId)
        intent.putExtra("latlng", latLng)
        startActivity(intent)
    }

    override fun onLocationChanged(latLng: LatLng) {
        helper.removeLocationUpdate()
        val fragment = supportFragmentManager.findFragmentById(R.id.rlHomeFragmentContainer)
        if (fragment != null && fragment is ExploreFragment) {
            fragment.onLocationChanged(latLng)
        }
    }

    override fun onLocationSettingsSuccess(settingsStates: LocationSettingsStates) {
        helper.requestLocationUpdate(applicationContext)
    }

    override fun onSettingsFailure(e: Exception) {
        helper.startOnResultionResult(this, e)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CHECK_PERMISSION) {
            if (helper.onRequestPermissionsResult(grantResults)) {
                helper.requestLocationSettings(this)
            } else {
                helper.showDialogOnPermissionFailed(this, supportFragmentManager)
            }
        }
    }

    override fun onAddProjectButtonClicked() {
        startActivityForResult(Intent(this, CreateProjectActivity::class.java), 5432)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_EDIT_PROFILE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                navigation.selectedItemId = R.id.navigation_profile
            }
        } else if (requestCode == REQUEST_CHECK_LOCATION_SETTINGS) run {
            if (!helper.onActivityResult(resultCode, data)) {
                helper.showDialogOnSettingsCancelled(supportFragmentManager)
                val fragment = supportFragmentManager.findFragmentById(R.id.rlHomeFragmentContainer)
                if (fragment != null && fragment is ExploreFragment) {
                    fragment.onLocationSettingsCancel()
                }
            }
        } else if (requestCode == REQUEST_CHECK_PERMISSION_SETTINGS) run {
            helper.requestPermission(
                this,
                supportFragmentManager
            )
        } else if (requestCode == 9898) {
            if (resultCode == Activity.RESULT_OK) {
                projectId = data?.getIntExtra("projectId", -1)
                navigation.selectedItemId = R.id.navigation_search
            }
        } else if (requestCode == 5432) {
            if (resultCode == Activity.RESULT_OK) {
                project = data?.getParcelableExtra("project")
                startActivityForResult(
                    MessageActivity.newIntent(
                        this,
                        R.drawable.ic_tick,
                        getString(R.string.project_created),
                        getString(R.string.you_have_successfully_created_your_project),
                        getString(R.string.continue_label),
                        MessageAction.PROJECT_CREATED
                    ), 7861
                )
            }
        } else if (requestCode == 7861) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, PropertyDetailActivity::class.java)
                intent.putExtra(
                    "enum",
                    PropertyDetailActivity.StartActivityFrom.CREATE_PROJECT_ACTIVITY
                )
                intent.putExtra("project", project)
                startActivityForResult(intent, 6772)
            }
        } else if (requestCode == 6772) {
            if (resultCode == Activity.RESULT_OK) {
                projectId = data?.getIntExtra("projectId", -1)
                navigation.selectedItemId = R.id.navigation_search
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        projectId = -1
    }

    private var projectId: Int? = -1
    private var project: Project? = null
}
