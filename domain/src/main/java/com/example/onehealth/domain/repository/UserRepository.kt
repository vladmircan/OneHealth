package com.example.onehealth.domain.repository

import com.example.onehealth.domain.model.local.UserLoginCredentialsModel
import com.example.onehealth.domain.model.local.UserModel
import com.example.onehealth.domain.model.local.UserRegistrationCredentialsModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val isLoggedIn: Flow<Boolean?>
    val userFlow: Flow<UserModel?>
    val currentUserId: String?

    suspend fun register(userCredentials: UserRegistrationCredentialsModel): Boolean

    suspend fun login(userCredentials: UserLoginCredentialsModel): Boolean

    suspend fun logout()
}