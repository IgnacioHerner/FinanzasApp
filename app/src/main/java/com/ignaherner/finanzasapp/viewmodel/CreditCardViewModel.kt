package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ignaherner.finanzasapp.data.local.CardUsage
import com.ignaherner.finanzasapp.data.CreditCardRepository
import com.ignaherner.finanzasapp.data.CreditTransactionRepository
import com.ignaherner.finanzasapp.data.local.CreditCardEntity
import com.ignaherner.finanzasapp.model.CreditCardWithUsage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreditCardViewModel(
    private val cardRepo: CreditCardRepository,
    private val txRepo: CreditTransactionRepository
) : ViewModel(){

    val cardsWithUsage: StateFlow<List<CreditCardWithUsage>> = combine(
        cardRepo.cards,
        txRepo.getUsage()
    ) { cards, usageList ->
        cards.map { card ->
            val usedAmount = usageList.find { it.cardId == card.id }?.used ?: 0.0
            CreditCardWithUsage(card, usedAmount)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun add(card: CreditCardEntity) {
        viewModelScope.launch {
            cardRepo.insert(card)
        }
    }

    fun update(card: CreditCardEntity) {
        viewModelScope.launch {
            cardRepo.update(card)
        }
    }

    fun delete(card: CreditCardEntity) {
        viewModelScope.launch {
            cardRepo.delete(card)
        }
    }

    fun getById(id: Int): Deferred<CreditCardEntity?> = viewModelScope.async {
        cardRepo.getById(id)
    }

}