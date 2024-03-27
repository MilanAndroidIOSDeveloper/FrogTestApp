package com.android.frogtest.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.frogtest.BioMetricManager
import com.android.frogtest.BioMetricPrompt
import com.android.frogtest.FrogTestApplication
import com.android.frogtest.ui.fragment.LoginFragment
import com.android.frogtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    val authSuccess = MutableLiveData<Boolean>()
    val authError = MutableLiveData<String>()
    private val auth = Firebase.auth

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authSuccess.value = true
                } else {
                    authError.value = task.exception?.localizedMessage
                }
            }
        }
    }

    fun signupWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authSuccess.value = true
                } else {
                    authError.value = task.exception?.localizedMessage
                }
            }
        }
    }
    fun authenticateWithBiometrics(lrFragment: Fragment, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        val biometricManager = BioMetricManager(FrogTestApplication.appContext)
        if (biometricManager.canAuthenticate()) {
            BioMetricPrompt(lrFragment).authenticate(onSuccess, onError)
        } else {
            onError(lrFragment.getString(R.string.fingerprint_hardware_not_available))
        }
    }


}