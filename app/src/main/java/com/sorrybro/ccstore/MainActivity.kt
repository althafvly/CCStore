package com.sorrybro.ccstore

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.sorrybro.ccstore.screen.MainScreen
import com.sorrybro.ccstore.ui.theme.CCStoreTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CCStoreTheme {
                MainScreen()
            }
        }
    }
}
