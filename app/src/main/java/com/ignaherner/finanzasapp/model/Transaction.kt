package com.ignaherner.finanzasapp.model

import java.time.LocalDate

enum class TransactionType {
    INCOME,
    EXPENSE
}

data class Transaction(
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val account: Account,
    val category: Category,
    val date: LocalDate, // Simplificado por ahora
)
