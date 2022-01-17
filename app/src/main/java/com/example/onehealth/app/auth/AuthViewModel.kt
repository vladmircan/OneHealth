package com.example.onehealth.app.auth

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.domain.model.local.UserCredentialsModel
import com.example.onehealth.domain.repository.UserRepository
import com.example.onehealth.domain.use_case.LoginUseCase
import com.example.onehealth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
        if (!userCredentials.isValid)
            return
        loginUseCase.perform(LoginUseCase.Params(userCredentials))
    }

    fun register(email: String, password: String, confirmationPassword: String) {
        if (password != confirmationPassword)
            return

        val userCredentials = UserCredentialsModel(email.trimEnd(), password.trimEnd())
        if (!userCredentials.isValid)
            return
        registerUseCase.perform(RegisterUseCase.Params(userCredentials)) { wasSignUpSuccessful ->
            onRegistrationSuccessful.postValue(wasSignUpSuccessful)
        }
    }

    private val UserCredentialsModel.isValid: Boolean
        get() = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length >= MIN_PASSWORD_LENGTH

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}