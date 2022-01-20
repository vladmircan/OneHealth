package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.UserLoginCredentialsModel
import com.example.onehealth.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val checkCredentialsForFormatErrorsUseCase: CheckCredentialsForFormatErrorsUseCase,
    private val userRepository: UserRepository
): UseCase<LoginUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        checkCredentialsForFormatErrorsUseCase(
            params = CheckCredentialsForFormatErrorsUseCase.Params(params.userCredentials)
        ).getOrNull()?.let { detectedFailure -> throw detectedFailure }
        return userRepository.login(params.userCredentials)
    }

    @JvmInline
    value class Params(val userCredentials: UserLoginCredentialsModel): UseCase.Params
}