package com.ignaherner.finanzasapp.model

import java.time.LocalDate

data class InstallmentGroup(
    val parentId: String,
    val title: String,
    val totalAmount: Double,
    val totalInstallments: Int,
    val paidInstallments: Int,
    val remainingInstallments: Int,
    val nextDueDate: LocalDate
)
