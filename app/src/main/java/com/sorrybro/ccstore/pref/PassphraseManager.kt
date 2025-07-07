package com.sorrybro.ccstore.pref

import android.content.Context
import androidx.core.content.edit
import com.sorrybro.ccstore.Constants
import dev.spght.encryptedprefs.EncryptedSharedPreferences
import dev.spght.encryptedprefs.MasterKey

object PassphraseManager {
    fun getOrCreateSecurePasskey(context: Context): String {
        val masterKey = MasterKey(context = context)
        val prefs = EncryptedSharedPreferences(
            context = context,
            fileName = Constants.PREF_NAME,
            masterKey = masterKey,
        )

        return prefs.getString(Constants.KEY_PASSPHRASE, null) ?: generateRandomPassword().also {
            prefs.edit { putString(Constants.KEY_PASSPHRASE, it) }
        }
    }

    fun generateRandomPassword(): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(32) { charset.random() }.joinToString("")
    }
}