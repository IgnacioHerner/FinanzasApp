package com.ignaherner.finanzasapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.model.Currency
import com.ignaherner.finanzasapp.model.RecurrenceType
import com.ignaherner.finanzasapp.model.TransactionType
import java.time.LocalDate

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: Category,
    val account: Account,
    val startDate: LocalDate,
    val recurrence: RecurrenceType,
    val currency: Currency = Currency.ARS,
    val lastExecuteDate: LocalDate? = null

)
