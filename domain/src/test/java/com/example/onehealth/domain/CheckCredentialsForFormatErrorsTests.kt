package com.example.onehealth.domain

import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.model.local.UserLoginCredentialsModel
import com.example.onehealth.domain.model.local.UserRegistrationCredentialsModel
import com.example.onehealth.domain.use_case.CheckCredentialsForFormatErrorsUseCase
import com.example.onehealth.domain.utils.Constants
import kotlinx.coroutines.runBlocking
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CheckCredentialsForFormatErrorsTests {

    val sut = CheckCredentialsForFormatErrorsUseCase()

    @Test
    fun `Login valid credentials are correct`() {
        runBlocking {
            val params = CheckCredentialsForFormatErrorsUseCase.Params(
                UserLoginCredentialsModel(
                    email = "a.b@c.com",
                    password = "1".repeat(Constants.MIN_PASSWORD_LENGTH)
                )
            )

            val identifiedError = sut(params).getOrNull()

            expectThat(identifiedError).isEqualTo(null)
        }
    }

    @Test
    fun `Registration valid credentials are correct`() {
        runBlocking {
            val params = CheckCredentialsForFormatErrorsUseCase.Params(
                UserRegistrationCredentialsModel(
                    email = "a.b@c.com",
                    password = "1".repeat(Constants.MIN_PASSWORD_LENGTH),
                    confirmationPassword = "1".repeat(Constants.MIN_PASSWORD_LENGTH),
                )
            )

            val identifiedError = sut(params).getOrNull()

            expectThat(identifiedError).isEqualTo(null)
        }
    }

    @Test
    fun `Invalid email format is detected`() {
        runBlocking {
            val params = CheckCredentialsForFormatErrorsUseCase.Params(
                UserLoginCredentialsModel(
                    email = "a.b.com",
                    password = "1".repeat(Constants.MIN_PASSWORD_LENGTH)
                )
            )

            val identifiedError = sut(params).getOrNull()

            expectThat(identifiedError).isEqualTo(Failure.AuthFailure.InvalidEmailFormat)
        }
    }

    @Test
    fun `Invalid password length is detected`() {
        runBlocking {
            val params = CheckCredentialsForFormatErrorsUseCase.Params(
                UserLoginCredentialsModel(
                    email = "a.b@c.com",
                    password = "1".repeat(Constants.MIN_PASSWORD_LENGTH - 1)
                )
            )

            val identifiedError = sut(params).getOrNull()

            expectThat(identifiedError).isEqualTo(Failure.AuthFailure.InvalidPasswordLength)
        }
    }

    @Test
    fun `Password do not match is detected`() {
        runBlocking {
            val params = CheckCredentialsForFormatErrorsUseCase.Params(
                UserRegistrationCredentialsModel(
                    email = "a.b@c.com",
                    password = "1".repeat(Constants.MIN_PASSWORD_LENGTH),
                    confirmationPassword = "2".repeat(Constants.MIN_PASSWORD_LENGTH),
                )
            )

            val identifiedError = sut(params).getOrNull()

            expectThat(identifiedError).isEqualTo(Failure.AuthFailure.PasswordsDoNotMatch)
        }
    }
}