package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.finanzasapp.data.RecurringTransactionRepository

class RecurringTransactionViewModelFactory (
    private val repository: RecurringTransactionRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create (modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(RecurringTransactionViewModel::class.java)) {
            return RecurringTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}