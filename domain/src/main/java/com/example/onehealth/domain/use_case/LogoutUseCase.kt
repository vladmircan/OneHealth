package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.NoResult
import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
): UseCase<UseCase.NoParams, NoResult>() {

    override suspend fun execute(params: NoParams) {
        userRepository.logout()
    }
}