package com.android.frogtest

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class BioMetricPrompt(private val fragment: Fragment) {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    fun authenticate(onSuccess: () -> Unit, onError: (errorCode: String) -> Unit) {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragment.getString(R.string.verify_that_it_s_you))
            .setSubtitle(fragment.getString(R.string.use_your_fingerprint_to_continue))
            .setNegativeButtonText(fragment.getString(R.string.cancel))
            .build()
        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(fragment.getString(R.string.biometric_authentication_error, errString))
            }

            override fun onAuthenticationFailed() {
                onError(fragment.getString(R.string.biometric_authentication_failed))
            }
        }


        biometricPrompt = BiometricPrompt(
            fragment,
            ContextCompat.getMainExecutor(FrogTestApplication.appContext),
            authenticationCallback
        )
        biometricPrompt.authenticate(promptInfo)


    }
}