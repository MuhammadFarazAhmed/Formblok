package com.incubasys.formblok.auth.extentions

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.makeInvisible
import com.incubasys.formblok.common.extenstions.makeVisible

fun ImageView.disable() {
    isEnabled = false
    setImageResource(R.drawable.floating_button_white)
}

fun ImageView.enable() {
    isEnabled = true
    setImageResource(R.drawable.floating_button_yellow)
}

fun EditText.validate(
    validator: (String) -> Boolean,
    message: String
) {
    this.doAfterTextChanged {
        this.error = if (validator(it.toString())) {
            null
        } else {
            message
        }
        /*if (it!!.isNotEmpty()) {
            textView.makeVisible()
        } else {
            textView.makeInvisible()
        }*/
    }
}

@BindingAdapter("validate")
fun setValidate(editText: EditText, validate: ObservableField<String>) {
    if (validate.get() != null && !validate.get().isNullOrEmpty()) {
        editText.error = validate.get()
    }
    //editText.requestFocus();
}

@BindingAdapter("validate2")
fun setOtherValidate(editText: EditText, validate: ObservableField<String>) {
    if (validate.get() != null) {
        editText.error = validate.get()
    }
    //editText.requestFocus();
}


fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
