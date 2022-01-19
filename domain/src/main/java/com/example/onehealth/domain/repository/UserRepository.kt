package com.example.onehealth.domain.repository

import com.example.onehealth.domain.model.local.UserCredentialsModel
import com.example.onehealth.domain.model.local.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val isLoggedIn: Flow<Boolean?>
    val user: UserModel?

    suspend fun register(userCredentials: UserCredentialsModel): Boolean

    suspend fun login(userCredentials: UserCredentialsModel): Boolean

    suspend fun logout()
}