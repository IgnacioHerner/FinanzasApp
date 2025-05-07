package com.ignaherner.finanzasapp.data

import com.ignaherner.finanzasapp.data.local.RecurringTransactionDao
import com.ignaherner.finanzasapp.data.local.TransactionDao
import com.ignaherner.finanzasapp.data.local.TransactionEntity
import com.ignaherner.finanzasapp.model.RecurrenceType
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class RecurringTransactionProcessor(
    private val recurringDao: RecurringTransactionDao,
    private val transactionDao: TransactionDao
) {

    suspend fun processRecurringTransactions() {
        val today = LocalDate.now()

        val recurrentes = recurringDao.getAllRecurring().first()

        recurrentes.forEach { r ->
            val lastExecuted = r.lastExecuteDate
            val shouldApply = when {
                lastExecuted == null -> true
                else -> when (r.recurrence) {
                    RecurrenceType.DAILY -> lastExecuted.plusDays(1) <= today
                    RecurrenceType.WEEKLY -> lastExecuted.plusWeeks(1) <= today
                    RecurrenceType.MONTHLY -> lastExecuted.plusMonths(1) <= today
                    RecurrenceType.YEARLY -> lastExecuted.plusYears(1) <= today
                }
            }

            if(shouldApply) {
                val nueva = TransactionEntity(
                    title = r.title,
                    amount = r.amount,
                    type = r.type,
                    category = r.category,
                    account = r.account,
                    date = today
                )
                transactionDao.insert(nueva)

                recurringDao.update(
                    r.copy(lastExecuteDate = today)
                )
            }
        }
    }
}