package com.example.onehealth.domain.core

sealed class Failure(val exception: Exception) {
    object Unauthorized: Failure(Exception("Unauthorized"))
    object InvalidCredentials: Failure(Exception("Invalid Credentials"))
    object InvalidEmailFormat: Failure(Exception("Invalid email format"))
    object InvalidPasswordLength: Failure(Exception("Invalid password length"))
    object InvalidPasswordFormat: Failure(Exception("Invalid password format"))
    object PasswordsDoNotMatch: Failure(Exception("Passwords do not match"))
    object Forbidden: Failure(Exception("Forbidden"))
    object NetworkConnectionFailure: Failure(Exception("NetworkConnectionFailure"))
    object TimeoutFailure: Failure(Exception("TimeoutFailure"))
    object NullResponse: Failure(Exception("NullResponse"))

    open class GenericFailure(featureException: Exception): Failure(featureException)

    override fun equals(other: Any?): Boolean {
        return this.exception == (other as? Failure)?.exception
    }

    override fun hashCode() = exception.hashCode()

    companion object
}