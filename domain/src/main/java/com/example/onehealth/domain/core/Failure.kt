package com.example.onehealth.domain.core

import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException

sealed class Failure(val exception: Exception) {
    object Unauthorized: Failure(Exception("Unauthorized"))
    object Forbidden: Failure(Exception("Forbidden"))
    object NetworkConnectionFailure: Failure(Exception("NetworkConnectionFailure"))
    object TimeoutFailure: Failure(Exception("TimeoutFailure"))
    object NullResponse: Failure(Exception("NullResponse"))

    open class GenericFailure(featureException: Exception): Failure(featureException)

    override fun equals(other: Any?): Boolean {
        return this.exception == (other as? Failure)?.exception
    }

    override fun hashCode() = exception.hashCode()

    companion object {
        fun fromThrowable(throwable: Throwable): Failure {
            return when (throwable) {
                is SocketException, is SocketTimeoutException -> TimeoutFailure
                is IOException -> NetworkConnectionFailure
                is KotlinNullPointerException -> NullResponse
                is Exception -> GenericFailure(throwable)
                else -> GenericFailure(RuntimeException(throwable.message, throwable))
            }
        }
    }
}