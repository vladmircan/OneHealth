package com.example.onehealth.app.auth

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.model.local.UserCredentialsModel
import com.example.onehealth.domain.repository.UserRepository
import com.example.onehealth.domain.use_case.LoginUseCase
import com.example.onehealth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    userRepository: UserRepository
): BaseViewModel() {

    val onRegistrationSuccessful = MutableLiveData<Boolean>()
    val isLoggedIn = userRepository.isLoggedIn

    fun login(email: String, password: String) {
        val userCredentials = UserCredentialsModel(email.trimEnd(), password.trimEnd())
        val credentialFormatError = checkCredentialsForFormatErrors(userCredentials)
        if (credentialFormatError != null) {
            handleFailure(credentialFormatError)
        } else {
            loginUseCase.perform(LoginUseCase.Params(userCredentials))

        }
    }

    fun register(email: String, password: String, confirmationPassword: String) {
        if (password != confirmationPassword) {
            handleFailure(Failure.PasswordsDoNotMatch)
            return
        }

        val userCredentials = UserCredentialsModel(email.trimEnd(), password.trimEnd())
        val credentialFormatError = checkCredentialsForFormatErrors(userCredentials)
        if (credentialFormatError != null) {
            handleFailure(credentialFormatError)
        } else {
            registerUseCase.perform(RegisterUseCase.Params(userCredentials)) { wasSignUpSuccessful ->
                onRegistrationSuccessful.postValue(wasSignUpSuccessful)
            }
        }
    }

    private fun checkCredentialsForFormatErrors(userCredentials: UserCredentialsModel): Failure? {
        return when {
            !userCredentials.hasValidEmail -> Failure.InvalidEmailFormat
            userCredentials.password.length < MIN_PASSWORD_LENGTH -> Failure.InvalidPasswordLength
            !userCredentials.hasValidPassword -> Failure.InvalidPasswordFormat
            else -> null
        }
    }

    override fun handleFailure(failure: Failure) {
        when (failure) {
            Failure.InvalidCredentials,
            Failure.InvalidEmailFormat,
            Failure.InvalidPasswordLength,
            Failure.PasswordsDoNotMatch -> {
                Timber.e(failure.exception)
                viewModelScope.launch { _failure.emit(failure) }
            }
            else -> super.handleFailure(failure)
        }
    }

    private val UserCredentialsModel.hasValidEmail: Boolean
        get() = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private val UserCredentialsModel.hasValidPassword: Boolean
        get() = password.length >= MIN_PASSWORD_LENGTH

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}