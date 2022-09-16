package com.incubasys.formblok.common.extenstions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.superscript
import com.incubasys.formblok.custom.CustomTypefaceSpan


inline fun FragmentManager.inTransaction(
    setAnimation: Boolean,
    animationType: String,
    func: FragmentTransaction.() -> Unit
) {
    val fragmentTransaction = beginTransaction()
    if (setAnimation) {
        if (animationType == "fade") {
            fragmentTransaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out, R.anim.fade_in, R.anim.fade_out
            )
        } else {
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    containerId: Int,
    backStackTag: String? = null,
    animation: Boolean = true,
    animationType: String = "swipe"
) {
    supportFragmentManager.inTransaction(animation, animationType) {
        add(containerId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    containerId: Int,
    backStackTag: String? = null,
    animation: Boolean = true,
    animationType: String = "swipe"
) {
    supportFragmentManager.inTransaction(animation, animationType) {
        replace(containerId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

fun AppCompatActivity.removeFragment(fragment: Fragment, animationType: String) {
    supportFragmentManager.inTransaction(true, animationType) {
        remove(fragment)
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun AppCompatActivity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyBoard(it) }
}

fun AppCompatActivity.showKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { showKeyBoard(it) }
}

fun Context.showKeyBoard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

fun AppCompatActivity.clearLightStatusBar() {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
        window.addFlags(FLAG_TRANSLUCENT_STATUS)
    }
}

@SuppressLint("ResourceType")
fun AppCompatActivity.changeStatusBarColorYellow(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = ContextCompat.getColor(context, R.color.colorPrimary)
    }
}

@SuppressLint("ResourceType")
fun AppCompatActivity.changeStatusBarColorWhite(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = ContextCompat.getColor(context, R.color.colorOnPrimary)
    }
}

fun getPolygonLatLngBounds(polygon: List<LatLng>): LatLngBounds {
    val centerBuilder = LatLngBounds.builder()
    polygon.forEach {
        centerBuilder.include(it)
    }
    return centerBuilder.build()
}

private fun CharSequence.toSpannable() = SpannableStringBuilder(this)
private fun String.toSpannable() = SpannableStringBuilder(this)

fun SpannableStringBuilder.spanText(span: Any): SpannableStringBuilder {
    setSpan(span, 0, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun CharSequence.foregroundColor(@ColorInt color: Int): SpannableStringBuilder {
    val span = ForegroundColorSpan(color)
    return toSpannable().spanText(span)
}

fun CharSequence.setMediumFont(context: Context): SpannableStringBuilder {
    val font = ResourcesCompat.getFont(context, R.font.visbycf_medium)
    val span = CustomTypefaceSpan("", font!!)
    return toSpannable().spanText(span)
}

fun CharSequence.setBoldFont(context: Context): SpannableStringBuilder {
    val font = ResourcesCompat.getFont(context, R.font.visbycf_bold)
    val span = CustomTypefaceSpan("", font!!)
    return toSpannable().spanText(span)
}

fun CharSequence.setHeavyFont(context: Context): SpannableStringBuilder {
    val font = ResourcesCompat.getFont(context, R.font.visbycf_heavy)
    val span = CustomTypefaceSpan("", font!!)
    return toSpannable().spanText(span)
}

fun CharSequence.setLightFont(context: Context): SpannableStringBuilder {
    val font = ResourcesCompat.getFont(context, R.font.visbycf_light)
    val span = CustomTypefaceSpan("", font!!)
    return toSpannable().spanText(span)
}

fun CharSequence.backgroundColor(@ColorInt color: Int): SpannableStringBuilder {
    val span = BackgroundColorSpan(color)
    return toSpannable().spanText(span)
}

fun CharSequence.relativeSize(size: Float): SpannableStringBuilder {
    val span = RelativeSizeSpan(size)
    return toSpannable().spanText(span)
}

fun CharSequence.bold(): SpannableStringBuilder {
    val span = StyleSpan(BOLD)
    return toSpannable().spanText(span)
}

fun CharSequence.normal(): SpannableStringBuilder {
    val span = StyleSpan(NORMAL)
    return toSpannable().spanText(span)
}

operator fun SpannableStringBuilder.plus(other: SpannableStringBuilder): SpannableStringBuilder {
    return this.append(other)
}

operator fun SpannableStringBuilder.plus(other: CharSequence): SpannableStringBuilder {
    return this + other.toSpannable()
}

operator fun SpannableStringBuilder.set(old: CharSequence, new: SpannableStringBuilder): SpannableStringBuilder {
    val index = indexOf(old.toString())
    return this.replace(index, index + old.length, new, 0, new.length)
}

fun spannedHeading(context: Context, text: String, otherText: String): SpannableStringBuilder {
    val appendedText = text.foregroundColor(ContextCompat.getColor(context, R.color.grey))
        .setLightFont(context)
    appendedText[otherText] =
        otherText.foregroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .relativeSize(1.5f)
            .setBoldFont(context)
    return appendedText
}

fun superscriptSpan(text: String): SpannableStringBuilder {
    text.isNotBlank().let {
        val builder = SpannableStringBuilder()
        builder.append(text)
        builder.append(superscript("m2"))
        return builder
    }
}

fun spannedHeadingReverse(context: Context, text: String, otherText: String, boldText: String): SpannableStringBuilder {
    val heading = text
        .relativeSize(1.2f)
        .foregroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    heading[otherText] =
        otherText
            .relativeSize(0.49f)
            .foregroundColor(ContextCompat.getColor(context, R.color.grey))
    heading[boldText] = boldText.bold()
    return heading
}

fun spannedPrivacyText(
    context: Context,
    fulltext: String,
    spannedText1: String,
    spannedText2: String
): SpannableStringBuilder {
    val appendedText = fulltext
        .foregroundColor(ContextCompat.getColor(context, R.color.grey))
    appendedText[spannedText1] = spannedText1.foregroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    appendedText[spannedText2] = spannedText2.foregroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    appendedText[spannedText1] = spannedText1.bold()
    appendedText[spannedText2] = spannedText2.bold()
    return appendedText
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun View.isGone(): Boolean = this.visibility == View.GONE

fun View.isInvisible(): Boolean = this.visibility == View.INVISIBLE

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun ImageView.loadDrawable(drawable: Drawable) {
    Glide.with(context)
        .load(drawable)
        .into(this)
}

fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
    val canvas =  Canvas()
    val bitmap = Bitmap.createBitmap (drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}










