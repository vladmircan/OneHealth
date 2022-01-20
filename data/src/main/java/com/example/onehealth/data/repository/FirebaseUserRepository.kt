package com.example.onehealth.data.repository

import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.model.local.UserLoginCredentialsModel
import com.example.onehealth.domain.model.local.UserModel
import com.example.onehealth.domain.model.local.UserRegistrationCredentialsModel
import com.example.onehealth.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

internal class FirebaseUserRepository: CoroutineScope, UserRepository {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + AppDispatchers.DEFAULT + CoroutineExceptionHandler { _, throwable ->
            Timber.e(
                throwable
            )
        }

    private val firebaseAuth = Firebase.auth

    private var firebaseUser: FirebaseUser? by Delegates.observable(null) { _, _, newValue ->
        onFirebaseUserChanged(newValue)
    }
    override val isLoggedIn = MutableStateFlow<Boolean?>(null)
    override var user: UserModel? = null
        private set

    init {
        firebaseUser = firebaseAuth.currentUser
        firebaseAuth.addAuthStateListener {
            firebaseUser = it.currentUser
        }
    }

    override suspend fun register(
        userCredentials: UserRegistrationCredentialsModel
    ): Boolean = suspendCoroutine { continuation ->
        firebaseAuth
            .createUserWithEmailAndPassword(userCredentials.email, userCredentials.password)
            .addOnSuccessListener { continuation.resume(true) }
            .addOnCanceledListener { continuation.resume(false) }
            .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
    }

    override suspend fun login(
        userCredentials: UserLoginCredentialsModel
    ): Boolean = suspendCoroutine { continuation ->
        firebaseAuth
            .signInWithEmailAndPassword(userCredentials.email, userCredentials.password)
            .addOnSuccessListener { continuation.resume(true) }
            .addOnCanceledListener { continuation.resume(false) }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    private fun onFirebaseUserChanged(firebaseUser: FirebaseUser?) {
        isLoggedIn.value = firebaseUser != null
        user = firebaseUser?.let {
            UserModel(
                userId = it.uid,
                email = it.email
            )
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}