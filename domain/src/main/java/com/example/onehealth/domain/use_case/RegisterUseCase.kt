package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.UserRegistrationCredentialsModel
import com.example.onehealth.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(
    private val checkCredentialsForFormatErrorsUseCase: CheckCredentialsForFormatErrorsUseCase,
    private val userRepository: UserRepository
): UseCase<RegisterUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        checkCredentialsForFormatErrorsUseCase(
            params = CheckCredentialsForFormatErrorsUseCase.Params(params.userCredentials)
        ).getOrNull()?.let { detectedFailure -> throw detectedFailure }
        return userRepository.register(params.userCredentials)
    }

    @JvmInline
    value class Params(val userCredentials: UserRegistrationCredentialsModel): UseCase.Params
}