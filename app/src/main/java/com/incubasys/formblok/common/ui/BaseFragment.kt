package com.incubasys.formblok.common.ui

import android.util.Log
import dagger.android.support.DaggerFragment

open class BaseFragment : DaggerFragment() {

    internal fun showProgress() {
        val prev = childFragmentManager.findFragmentByTag("dialog")
        if (prev == null) {
            ProgressDialogFragment.newInstance("", "").show(childFragmentManager, "dialog")
        }
    }

    internal fun dismissProgress() {
        try {
            val prev = childFragmentManager.findFragmentByTag("dialog")
            if (prev is ProgressDialogFragment) {
                prev.dismissAllowingStateLoss()
            }
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

}
