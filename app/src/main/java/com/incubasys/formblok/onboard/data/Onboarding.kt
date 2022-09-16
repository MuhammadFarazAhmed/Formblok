package com.incubasys.formblok.onboard.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

class Onboarding : Parcelable {
    var resId: Int? = null
    var upperText: String? = null
    var lowerText: String? = null

    constructor() : super() {}

    @SuppressLint("ParcelClassLoader")
    internal constructor(`in`: Parcel) {
        resId = `in`.readValue(null) as Int
        upperText = `in`.readValue(null) as String
        lowerText = `in`.readValue(null) as String
    }

    constructor(resId: Int, upperText: String, lowerText: String) {
        this.resId = resId
        this.upperText = upperText
        this.lowerText = lowerText

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeValue(resId)
        parcel.writeValue(upperText)
        parcel.writeValue(lowerText)

    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<Onboarding> = object : Parcelable.Creator<Onboarding> {
            override fun createFromParcel(`in`: Parcel): Onboarding {
                return Onboarding(`in`)
            }

            override fun newArray(size: Int): Array<Onboarding?> {
                return arrayOfNulls(size)
            }
        }
    }
}
