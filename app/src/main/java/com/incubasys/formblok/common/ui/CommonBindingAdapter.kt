package com.incubasys.formblok.common.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.incubasys.formblok.R
import com.incubasys.formblok.custom.RoundedCornersTransformation
import com.incubasys.formblok.di.GlideApp
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.projects.data.model.Project
import java.io.File
import java.text.DecimalFormat


@BindingAdapter(
    value = ["android:src", "placeholderRes", "errorRes", "android:scaleType", "isCircle", "isRoundCorners"],
    requireAll = false
)
fun loadImageDrawable(
    imageView: ImageView?,
    image: Any?,
    placeholderResId: Drawable?,
    errorResId: Drawable?,
    scaleType: ImageView.ScaleType?,
    isCircle: Boolean = false,
    isRoundCorners: Boolean = false
) {
    val requestManager = imageView?.let { GlideApp.with(it) }
    var requestBuilder: RequestBuilder<*>? = null
    image?.apply {
        when (image) {
            is String -> {
                val url = image as String?
                requestBuilder = if (url!!.startsWith("http://") || url.startsWith("https://")) {
                    requestManager?.load(url)
                } else {
                    requestManager?.load(File(url))
                }
            }
            is Uri -> requestBuilder = requestManager?.load(image as Uri?)
            is Drawable -> requestBuilder = requestManager?.load(image as Drawable?)
            is Bitmap -> requestBuilder = requestManager?.load(image as Bitmap?)
            is Int -> requestBuilder = requestManager?.load(image as Int?)
            is File -> requestBuilder = requestManager?.load(image as File?)
            is Array<*> -> requestBuilder = requestManager?.load(image as Array<*>?)
            else -> requestBuilder = requestManager?.load(image)
        }
    }

    var options = RequestOptions()
    options = options.override(imageView!!.width, imageView.height)
    placeholderResId?.apply {
        requestBuilder = requestManager?.load(placeholderResId)
    }

    scaleType?.apply {
        options = when (scaleType) {
            ImageView.ScaleType.CENTER_CROP -> options.centerCrop()
            ImageView.ScaleType.FIT_CENTER -> options.fitCenter()
            ImageView.ScaleType.CENTER_INSIDE -> options.centerInside()
            ImageView.ScaleType.MATRIX -> TODO()
            ImageView.ScaleType.FIT_XY -> TODO()
            ImageView.ScaleType.FIT_START -> TODO()
            ImageView.ScaleType.FIT_END -> TODO()
            ImageView.ScaleType.CENTER -> TODO()
        }
    }

    if (isCircle) {
        options = options.circleCrop()
    }
    if (isRoundCorners) {
        options = RequestOptions.bitmapTransform(
            MultiTransformation(
                CenterCrop(),
                RoundedCornersTransformation(34, 0)
            )
        )
    }
    imageView.let {
        requestBuilder?.apply(options.placeholder(placeholderResId).error(errorResId))
            ?.into(it)
    }

}


//@BindingAdapter("htmlText")
//fun setHtmlText(textView: TextView, text: String) {
//    if (!TextUtils.isEmpty(text)) {
//        textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
//    }
//}


@BindingAdapter("ImageBaseOnNotificationType")
fun imageType(imageView: ImageView, notificationOutput: NotificationOutput) {
    when (notificationOutput.notificationType) {
        0 -> {
            loadImage(imageView, R.drawable.ic_address)
        }
        1 -> {
            loadImage(imageView, R.drawable.ic_project)
        }
    }
}

@BindingAdapter("ImageBaseOnProjectType")
fun imageTypeForProject(imageView: ImageView, project: Project) {
    when (project.type) {
        0 -> {
            loadImage(imageView, R.drawable.build)
        }
        1 -> {
            loadImage(imageView, R.drawable.renno)
        }
        2 -> {
            loadImage(imageView, R.drawable.checkbox_selector)
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["width", "length", "showOnlyArea"], requireAll = false)
fun calculateArea(textView: TextView, width: Double, length: Double, showOnlyArea: Boolean = false) {

    val area = width * length
    val finalArea = DecimalFormat("##.##").format(area)
    val displayValue = "$width x $length = $finalArea"

    val stringBuilder = SpannableStringBuilder()
    if (showOnlyArea) {
        stringBuilder.append(finalArea)
    } else {
        stringBuilder.append(displayValue)
    }
    stringBuilder.append(superscript("m2"))

    textView.text = stringBuilder
}

@BindingAdapter(value = ["isChecked"])
fun isChecked(view: ImageView, isChecked: Boolean) {
    view.isSelected = isChecked
}

@BindingAdapter(value = ["validateCalculation"])
fun validateCalculation(view: TextView, isCalculationExceeds: ObservableBoolean) {
    if (isCalculationExceeds.get()) {
        view.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_red_light))
    } else {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["spannedText"])
fun spannedText(view: TextView, text: Double) {
    val builder = SpannableStringBuilder()
    val dText = DecimalFormat("##.##").format(text)
    builder.append(dText)
    builder.append(superscript("m2"))
    view.text = builder
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["percentageText"])
fun suffixPercent(view: TextView, text: Double) {
    val builder = StringBuilder()
    val dText = DecimalFormat("##.##").format(text)
    builder.append(dText)
    builder.append("%")
    view.text = "($builder)"
}

fun addPlusIcon(text: String): String {
    val builder = StringBuilder()
    builder.append(text)
    builder.append("+")
    return builder.toString()
}

fun prefixDollarSign(text: Double) :String {
    val builder = StringBuilder()
    builder.append("$")
    builder.append(text.toInt())
    return builder.toString()
}

@BindingAdapter(value = ["kmText"])
fun suffixKm(view: TextView, text: Double) {
    val builder = StringBuilder()
    val dText = DecimalFormat("##.##").format(text)
    builder.append(dText)
    builder.append("km")
    view.text = builder
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["spannedText", "isSpanned"])
fun spannedTextForProjectListItem(view: TextView, text: String, isSpanned: Boolean) {
    if (isSpanned) {
        val builder = SpannableStringBuilder()
        //val dText = DecimalFormat("##.##").format(text)
        builder.append(text)
        builder.append(superscript("m2"))
        view.text = builder
    } else {
        view.text = text
    }
}

fun superscript(text: String): SpannableStringBuilder {
    val builder = SpannableStringBuilder(text)
    builder.setSpan(
        SuperscriptSpan(),
        text.indexOf("2"),
        text.indexOf("2") + "2".length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    val relativeSizeSpan = RelativeSizeSpan(.5f)
    builder.setSpan(
        relativeSizeSpan,
        text.indexOf("2"),
        text.indexOf("2") + "2".length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return builder
}

private fun loadImage(imageView: ImageView, drawable: Int) {
    Glide.with(imageView.context)
        .load(drawable)
        .into(imageView)
}

/*@BindingAdapter("data")
fun setRecyclerViewProperties(recyclerView: RecyclerView, items: MutableList<PropertyMinimal>) {
    if (recyclerView.adapter is PropertyAdapter) {
        items.let {
            (recyclerView.adapter as PropertyAdapter).submitList(items)
        }
    }
}*/

@BindingAdapter(value = ["calculatedArea"])
fun updateDecimalFormatValue(tvCalculated: TextView, calculatedArea: MutableLiveData<Double>) {
    tvCalculated.text = SpannableStringBuilder().apply {
        append(DecimalFormat("##.##").format(calculatedArea.value).toString())
        append(superscript("m2"))
    }
}

@BindingAdapter(value = ["propertyAttrChanged"])
fun setPropertyListener(editText: EditText, listener: InverseBindingListener?) {
    if (listener != null) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                listener.onChange()
            }
        })
    }
}

@BindingAdapter(value = ["property"])
fun showPropertyValue(editText: EditText, value: MutableLiveData<Double>) {
    editText.text = SpannableStringBuilder().apply {
        append(DecimalFormat("##.##").format(value.value))
        append(superscript("m2"))
    }
}

@InverseBindingAdapter(attribute = "property")
fun getRealValue(editText: EditText): Double {
    return if (editText.text.toString() != "") {
        if (editText.text.toString().contains("m2")) {
            editText.text.delete(editText.length() - 2, editText.length())
        }
        editText.text.toString().toDouble()
    } else {
        0.0
    }
}



