package com.ignaherner.finanzasapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ignaherner.finanzasapp.model.Category
import java.time.LocalDate

@Entity(tableName = "credit_transactions")
data class CreditTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardId: Int,
    val title: String,
    val amount: Double,
    val category: Category,
    val date: LocalDate
)