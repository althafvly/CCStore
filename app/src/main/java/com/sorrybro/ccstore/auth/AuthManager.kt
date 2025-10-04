package com.sorrybro.ccstore.auth

import android.app.Activity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.sorrybro.ccstore.R
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AuthManager {
    fun canAuthenticate(activity: Activity): Boolean {
        val biometricManager = BiometricManager.from(activity)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        return canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(
        activity: Activity,
        executor: Executor = Executors.newSingleThreadExecutor(),
        onSuccess: () -> Unit,
        onError: ((errorCode: Int, errString: CharSequence) -> Unit)? = null
    ) {
        if (canAuthenticate(activity)) {
            onSuccess()
            return
        }

        val biometricPrompt = BiometricPrompt(
            activity as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    activity.runOnUiThread {
                        onSuccess()
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_CANCELED
                    ) {
                        activity.finishAffinity()
                    } else {
                        activity.runOnUiThread {
                            onError?.invoke(errorCode, errString)
                            onSuccess()
                        }
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.authenticate_title))
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}