package com.incubasys.formblok.util

import android.util.Log
import com.google.gson.Gson
import com.incubasys.formblok.common.data.model.Message
import retrofit2.HttpException

import javax.inject.Inject
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

import com.google.android.gms.common.internal.Preconditions.checkNotNull

@Suppress("NAME_SHADOWING")
class ParseErrors @Inject
constructor(private val gson: Gson) {

    fun interpretErrors(throwable: Throwable): Message {
        checkNotNull(throwable)
        var errorMessage = Message()
        if (throwable is HttpException) {
            errorMessage = parseHttpErrors(throwable, errorMessage)
        } else {
            throwable.printStackTrace()
            errorMessage.code = 900
            if (throwable is UnknownHostException) {
                errorMessage.code = 901
                errorMessage.message = "Unable to connect to internet"
            }
            if (throwable is ConnectException || throwable is SocketTimeoutException) {
                errorMessage.code = 902
                errorMessage.message = "Unable to connect, Please retry"
            }
        }
        return errorMessage
    }

    private fun parseHttpErrors(throwable: HttpException, errorObject: Message): Message {
        var errorObject = errorObject
        val statusCode = throwable.code()
        errorObject.code = statusCode
        try {
            val title = throwable.response().message()
            val body = throwable.response().errorBody()!!.string()
            Log.v("errorResponse", title)
            Log.v("errorBody", body)
            if (statusCode in 400..499) {
                if (!body.contains("</html>")) {
                    errorObject = gson.fromJson(body, Message::class.java)
                } else {
                    errorObject.message = title
                }
            } else if (statusCode in 500..599) {
                errorObject.message = "Server Error"
            } else {
                errorObject.message = String.format("%s Body: %s", title, body)
            }
        } catch (e: IOException) {
            Log.e("error", e.message, e)
        }

        return errorObject
    }

}
