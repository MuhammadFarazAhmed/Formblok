package com.incubasys.formblok.util

import android.content.Context
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns

object Utils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= 8
    }

    fun isValidPhone(target: String): Boolean {
        return !TextUtils.isEmpty(target) && TextUtils.isDigitsOnly(target) && target.length == 12
    }

    fun mergeNames(firstName: String, lastName: String): String {
        return String.format("%s %s", firstName, lastName)
    }
}
