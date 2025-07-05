package com.sorrybro.ccstore.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.db.CardDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = CardDatabase.Companion.getDatabase(app).cardDao()

    val cards = dao.getAll().stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())

    fun save(card: CardEntity) = viewModelScope.launch { dao.insert(card) }
    fun delete(card: CardEntity) = viewModelScope.launch { dao.delete(card) }
}
