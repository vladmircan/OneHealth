package com.example.onehealth.domain.core

sealed class Failure(val exception: Exception): Throwable(cause = exception) {
    object Unauthorized: Failure(Exception("Unauthorized"))
    object Forbidden: Failure(Exception("Forbidden"))
    object NetworkConnectionFailure: Failure(Exception("NetworkConnectionFailure"))
    object TimeoutFailure: Failure(Exception("TimeoutFailure"))
    object NullResponse: Failure(Exception("NullResponse"))

    sealed interface AuthFailure {
        object InvalidCredentials: Failure(Exception("Invalid Credentials")), AuthFailure
        object InvalidEmailFormat: Failure(Exception("Invalid email format")), AuthFailure
        object InvalidPasswordLength: Failure(Exception("Invalid password length")), AuthFailure
        object InvalidPasswordFormat: Failure(Exception("Invalid password format")), AuthFailure
        object PasswordsDoNotMatch: Failure(Exception("Passwords do not match")), AuthFailure
    }

    sealed interface MeasurementFailure {
        object MeasurementValueOutsideRange:
            Failure(Exception("Measurement value is outside the acceptable range")),
            MeasurementFailure

        object NumberFormat: Failure(Exception("NumberFormat")), MeasurementFailure
    }

    open class GenericFailure(featureException: Exception): Failure(featureException)

    override fun equals(other: Any?): Boolean {
        return this.exception == (other as? Failure)?.exception
    }

    override fun hashCode() = exception.hashCode()

    companion object
}