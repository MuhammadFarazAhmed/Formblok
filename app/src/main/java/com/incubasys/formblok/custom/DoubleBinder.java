package com.incubasys.formblok.custom;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class DoubleBinder {

    @BindingAdapter(value = "realValueAttrChanged")
    public static void setListener(EditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("realValue")
    public static void setRealValue(EditText view, double value) {
        view.setText(String.valueOf(value));
    }

    @InverseBindingAdapter(attribute = "realValue")
    public static double getRealValue(EditText editText) {
        if (!editText.getText().toString().equals("")) {
            return Double.parseDouble(editText.getText().toString());
        } else {
            return 0.0;
        }
    }
}
