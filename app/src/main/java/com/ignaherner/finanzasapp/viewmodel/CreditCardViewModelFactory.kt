package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.finanzasapp.data.CreditCardRepository
import com.ignaherner.finanzasapp.data.CreditTransactionRepository

class CreditCardViewModelFactory(
    private val cardRepo: CreditCardRepository,
    private val txRepo: CreditTransactionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreditCardViewModel::class.java)) {
            return CreditCardViewModel(cardRepo, txRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
