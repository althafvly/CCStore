package com.sorrybro.ccstore.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.db.CardDao
import com.sorrybro.ccstore.db.CardDatabase
import com.sorrybro.ccstore.pref.PassphraseManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(app: Application) : AndroidViewModel(app) {
    private val passphrase: String by lazy {
        PassphraseManager.getOrCreateSecurePasskey(app)
    }

    private val dao: CardDao by lazy {
        CardDatabase.getInstance(app, passphrase).cardDao()
    }

    val cards = dao.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun save(card: CardEntity) = viewModelScope.launch { dao.insert(card) }
    fun delete(card: CardEntity) = viewModelScope.launch { dao.delete(card) }
    fun update(card: CardEntity) = viewModelScope.launch { dao.update(card) }
}
