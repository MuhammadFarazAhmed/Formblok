package com.incubasys.formblok.common.ui

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            val fragment = supportFragmentManager
                .findFragmentByTag(
                    fm.getBackStackEntryAt(
                        supportFragmentManager.backStackEntryCount - 1
                    ).name
                )
            if (fragment != null && fragment.isVisible) {
                val childFm = fragment.childFragmentManager
                if (childFm.backStackEntryCount > 0) {
                    childFm.popBackStack()
                    return
                }
            }
        } else {
            for (fragment in supportFragmentManager.fragments) {
                if (fragment != null && fragment.isVisible) {
                    val childFm = fragment.childFragmentManager
                    if (childFm.backStackEntryCount > 0) {
                        childFm.popBackStack()
                        return
                    }
                }
            }
        }
        super.onBackPressed()
    }

    internal fun showProgress() {
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev == null) {
            ProgressDialogFragment.newInstance("", "").show(supportFragmentManager, "dialog")
        }
    }

    protected fun dismissProgress() {
        try {
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev is ProgressDialogFragment) {
                prev.dismissAllowingStateLoss()
            }
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

}