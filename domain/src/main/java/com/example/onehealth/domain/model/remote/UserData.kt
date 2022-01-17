package com.example.onehealth.domain.model.remote

import com.example.onehealth.domain.core.DomainMappable
import com.example.onehealth.domain.model.local.UserModel

data class UserData(
    val userId: String? = null,
    val username: String? = null
): DomainMappable<UserModel> {

    override fun toDomainModel(): UserModel {
        return UserModel(userId, username)
    }
}