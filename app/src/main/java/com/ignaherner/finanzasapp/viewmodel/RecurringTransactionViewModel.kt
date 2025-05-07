package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ignaherner.finanzasapp.data.RecurringTransactionRepository
import com.ignaherner.finanzasapp.data.local.RecurringTransactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecurringTransactionViewModel(
    private val repository: RecurringTransactionRepository
) : ViewModel() {

    val recurringTransaction: StateFlow<List<RecurringTransactionEntity>> =
        repository.getAllRecurring()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insert(recurring: RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.insertRecurring(recurring)
        }
    }

    fun delete(recurring: RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.deleteRecurring(recurring)
        }
    }

    fun update(recurring: RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.updateRecurring(recurring)
        }
    }


}