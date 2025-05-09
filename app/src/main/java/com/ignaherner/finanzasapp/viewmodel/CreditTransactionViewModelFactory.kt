package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.finanzasapp.data.CreditTransactionRepository

class CreditTransactionViewModelFactory(
    private val repository: CreditTransactionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreditTransactionViewModel::class.java)) {
            return CreditTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}