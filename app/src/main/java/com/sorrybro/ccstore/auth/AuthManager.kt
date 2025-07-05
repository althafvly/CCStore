package com.sorrybro.ccstore.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.sorrybro.ccstore.R
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AuthManager {
    private var lastAuthenticatedTime: Long = 0L
    private const val TIMEOUT_MS = 60_000L // 60 seconds

    fun updateLastPauseTime() {
        lastAuthenticatedTime = System.currentTimeMillis()
    }

    fun needsReAuth(): Boolean {
        val elapsed = System.currentTimeMillis() - lastAuthenticatedTime
        return lastAuthenticatedTime > 0 && elapsed > TIMEOUT_MS
    }

    fun authenticate(
        context: Context,
        executor: Executor = Executors.newSingleThreadExecutor(),
        onSuccess: () -> Unit,
        onError: ((errorCode: Int, errString: CharSequence) -> Unit)? = null
    ) {
        val biometricManager = BiometricManager.from(context)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            onSuccess()
            return
        }

        val biometricPrompt = BiometricPrompt(
            context as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    context.runOnUiThread {
                        onSuccess()
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_CANCELED
                    ) {
                        context.finishAffinity()
                    } else {
                        context.runOnUiThread {
                            onError?.invoke(errorCode, errString)
                            onSuccess()
                        }
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.authenticate_title))
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}