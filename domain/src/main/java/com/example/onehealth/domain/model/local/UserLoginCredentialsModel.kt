package com.example.onehealth.domain.model.local

interface UserCredentialsModel {
    val email: String
    val password: String
}

data class UserLoginCredentialsModel(
    override val email: String,
    override val password: String
): UserCredentialsModel

data class UserRegistrationCredentialsModel(
    override val email: String,
    override val password: String,
    val confirmationPassword: String
): UserCredentialsModel {

    fun doPasswordsMatch(): Boolean {
        return password == confirmationPassword
    }
}