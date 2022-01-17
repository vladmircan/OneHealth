package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.UserCredentialsModel
import com.example.onehealth.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
): UseCase<LoginUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        return userRepository.login(params.userCredentials)
    }

    @JvmInline
    value class Params(val userCredentials: UserCredentialsModel): UseCase.Params
}