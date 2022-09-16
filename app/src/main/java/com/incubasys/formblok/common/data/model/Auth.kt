package com.incubasys.formblok.common.data.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*


class Auth @SuppressLint("ParcelClassLoader") internal constructor(`in`: Parcel) : Parcelable {
    /**
     * Get sid
     *
     * @return sid
     */
    @SerializedName("sid")
    var sid: Int
    /**
     * Get stoken
     *
     * @return stoken
     */
    @SerializedName("stoken")
    var stoken: String
    /**
     * Get user
     *
     * @return user
     */
    @SerializedName("user")
    var user: User


    init {
        sid = `in`.readValue(null) as Int
        stoken = `in`.readValue(null) as String
        user = `in`.readValue(User::class.java.classLoader) as User
    }

    fun sid(sid: Int): Auth {
        this.sid = sid
        return this
    }

    fun stoken(stoken: String): Auth {
        this.stoken = stoken
        return this
    }

    fun user(user: User): Auth {
        this.user = user
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val auth = other as Auth?
        return this.sid == auth!!.sid &&
                this.stoken == auth.stoken &&
                this.user == auth.user
    }

    override fun hashCode(): Int {
        return Objects.hash(sid, stoken, user)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class Auth {\n")

        sb.append("    sid: ").append(toIndentedString(sid)).append("\n")
        sb.append("    stoken: ").append(toIndentedString(stoken)).append("\n")
        sb.append("    user: ").append(toIndentedString(user)).append("\n")
        sb.append("}")
        return sb.toString()
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */

    private fun toIndentedString(o: Any?): String {
        return o?.toString()?.replace("\n", "\n    ") ?: "null"
    }

    override fun writeToParcel(out: Parcel, flags: Int) {


        out.writeValue(sid)
        out.writeValue(stoken)
        out.writeValue(user)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<Auth> = object : Parcelable.Creator<Auth> {
            override fun createFromParcel(`in`: Parcel): Auth {
                return Auth(`in`)
            }

            override fun newArray(size: Int): Array<Auth?> {
                return arrayOfNulls(size)
            }
        }
    }
}