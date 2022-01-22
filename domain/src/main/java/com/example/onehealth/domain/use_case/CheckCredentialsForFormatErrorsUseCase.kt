package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.UserCredentialsModel
import com.example.onehealth.domain.model.local.UserRegistrationCredentialsModel
import com.example.onehealth.domain.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckCredentialsForFormatErrorsUseCase @Inject constructor():
    UseCase<CheckCredentialsForFormatErrorsUseCase.Params, Failure?>() {

    override suspend fun execute(params: Params): Failure? {
        val credentials = params.credentials
        return when {
            !credentials.hasValidEmail -> Failure.AuthFailure.InvalidEmailFormat
            credentials.password.length < Constants.MIN_PASSWORD_LENGTH -> Failure.AuthFailure.InvalidPasswordLength
            !credentials.hasValidPassword -> Failure.AuthFailure.InvalidPasswordFormat
            credentials is UserRegistrationCredentialsModel && !credentials.doPasswordsMatch() -> {
                Failure.AuthFailure.PasswordsDoNotMatch
            }
            else -> null
        }
    }

    private val UserCredentialsModel.hasValidEmail: Boolean
        get() = email.isNotBlank() && EMAIL_ADDRESS_REGEX.matches(email)

    private val UserCredentialsModel.hasValidPassword: Boolean
        get() = true

    @JvmInline
    value class Params(val credentials: UserCredentialsModel): UseCase.Params

    companion object {
        val EMAIL_ADDRESS_REGEX = Regex(
            pattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
        )
    }
}