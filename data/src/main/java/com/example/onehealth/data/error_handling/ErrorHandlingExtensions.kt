package com.example.onehealth.data.error_handling

import com.example.onehealth.domain.core.Failure
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException

fun Failure.Companion.fromThrowable(throwable: Throwable): Failure {
    return when (throwable) {
        is Failure -> throwable
        is SocketException, is SocketTimeoutException -> Failure.TimeoutFailure
        is NumberFormatException -> Failure.MeasurementFailure.NumberFormat
        is IOException -> Failure.NetworkConnectionFailure
        is FirebaseAuthInvalidUserException -> Failure.AuthFailure.InvalidCredentials
        is KotlinNullPointerException -> Failure.NullResponse
        is Exception -> Failure.GenericFailure(throwable)
        else -> Failure.GenericFailure(RuntimeException(throwable.message, throwable))
    }
}