package com.android.frogtest

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class BioMetricPrompt(private val fragment: Fragment) {


    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    fun authenticate(onSuccess: () -> Unit, onError: (errorDesc: String) -> Unit) {
        setupPromptInfo()

        val authenticationCallback = createAuthenticationCallback(onSuccess, onError)

        createBiometricPrompt(authenticationCallback)

        showBiometricPrompt()
    }

    private fun setupPromptInfo() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragment.getString(R.string.verify_that_it_s_you))
            .setSubtitle(fragment.getString(R.string.use_your_fingerprint_to_continue))
            .setNegativeButtonText(fragment.getString(R.string.cancel))
            .build()
    }

    private fun createAuthenticationCallback( onSuccess: () -> Unit, onError: (errorDesc: String) -> Unit)
            : BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorDesc: Int, errString: CharSequence) {
                super.onAuthenticationError(errorDesc, errString)
                onError(fragment.getString(R.string.biometric_authentication_error, errString))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError(fragment.getString(R.string.biometric_authentication_failed))
            }
        }
    }

    private fun createBiometricPrompt(authenticationCallback: BiometricPrompt.AuthenticationCallback) {
        biometricPrompt = BiometricPrompt(
            fragment,
            ContextCompat.getMainExecutor(FrogTestApplication.appContext),
            authenticationCallback
        )
    }

    private fun showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }
}