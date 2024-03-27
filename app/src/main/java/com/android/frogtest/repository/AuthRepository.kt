package com.android.frogtest.repository

import com.android.frogtest.FrogTestApplication
import com.android.frogtest.R
import com.android.frogtest.models.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val auth = Firebase.auth

    suspend fun login(email: String, password: String): Resource<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: FrogTestApplication.appContext.getString(R.string.login_failed))
        }
    }

    suspend fun signUp(email: String, password: String): Resource<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: FrogTestApplication.appContext.getString(R.string.sign_up_failed))
        }
    }
}