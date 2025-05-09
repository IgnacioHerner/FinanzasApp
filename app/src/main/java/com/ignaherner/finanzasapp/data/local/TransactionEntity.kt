package com.ignaherner.finanzasapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.model.Currency
import com.ignaherner.finanzasapp.model.TransactionType
import java.time.LocalDate

@Entity (tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val account: Account,
    val category: Category,
    val date: LocalDate,
    val isPaid: Boolean = true,
    val currency: Currency = Currency.ARS
)
