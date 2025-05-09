package com.ignaherner.finanzasapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ignaherner.finanzasapp.model.Category
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "credit_transactions")
data class CreditTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardId: Int,
    val title: String,
    val amount: Double,
    val category: Category,
    val date: LocalDate,
    val totalInstallments: Int = 1,
    val installment: Int = 1,
    val isPaid: Boolean = false,
    val parentId: String = UUID.randomUUID().toString()
)