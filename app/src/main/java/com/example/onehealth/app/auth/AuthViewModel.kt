package com.example.onehealth.app.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.model.local.UserLoginCredentialsModel
import com.example.onehealth.domain.model.local.UserRegistrationCredentialsModel
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
        val userCredentials = UserLoginCredentialsModel(
            email.trimEnd(),
            password.trimEnd()
        )
        loginUseCase.perform(LoginUseCase.Params(userCredentials))
    }

    fun register(email: String, password: String, confirmationPassword: String) {
        registerUseCase.perform(
            input = RegisterUseCase.Params(
                UserRegistrationCredentialsModel(
                    email.trimEnd(),
                    password.trimEnd(),
                    confirmationPassword.trimEnd()
                )
            ),
            onSuccess = { wasSignUpSuccessful ->
                onRegistrationSuccessful.postValue(wasSignUpSuccessful)
            }
        )
    }

    override fun handleFailure(failure: Failure) {
        when (failure) {
            is Failure.AuthFailure -> {
                Timber.e(failure.exception)
                viewModelScope.launch { _failure.emit(failure) }
            }
            else -> super.handleFailure(failure)
        }
    }
}