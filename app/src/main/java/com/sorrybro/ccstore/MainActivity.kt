package com.sorrybro.ccstore

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.sorrybro.ccstore.auth.AuthManager
import com.sorrybro.ccstore.screen.MainScreen
import com.sorrybro.ccstore.ui.theme.CCStoreTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observe foreground state
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

        // First-time authentication
        AuthManager.authenticate(
            context = this,
            onSuccess = { onSuccess() }
        )
    }

    fun onSuccess() {
        setContent {
            CCStoreTheme {
                MainScreen()
            }
        }
    }

    private val appLifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                // Save timestamp when app goes to background
                AuthManager.updateLastPauseTime()
            }

            Lifecycle.Event.ON_RESUME -> {
                if (AuthManager.needsReAuth()) {
                    AuthManager.authenticate(
                        context = this,
                        onSuccess = { onSuccess() }
                    )
                }
            }

            else -> Unit
        }
    }
}