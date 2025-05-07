package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.TransactionEntity
import com.ignaherner.finanzasapp.model.TransactionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransaction
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun getBalance(): Double = transactions.value
        .filter { it.isPaid }
        .sumOf {
            if (it.type == TransactionType.INCOME) it.amount else -it.amount
        }

    fun getIncome(): Double = transactions.value
        .filter { it.type == TransactionType.INCOME }
        .sumOf { it.amount }

    fun getExpenses(): Double = transactions.value
        .filter { it.type == TransactionType.EXPENSE }
        .sumOf { it.amount }

}
