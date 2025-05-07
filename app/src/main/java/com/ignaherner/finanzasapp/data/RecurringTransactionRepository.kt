package com.ignaherner.finanzasapp.data

import com.ignaherner.finanzasapp.data.local.RecurringTransactionDao
import com.ignaherner.finanzasapp.data.local.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

class RecurringTransactionRepository(private val dao: RecurringTransactionDao) {

    fun getAllRecurring(): Flow<List<RecurringTransactionEntity>> = dao.getAllRecurring()

    suspend fun insertRecurring(recurringTransaction: RecurringTransactionEntity) {
        dao.insert(recurringTransaction)
    }

    suspend fun deleteRecurring(recurringTransaction: RecurringTransactionEntity) {
        dao.delete(recurringTransaction)
    }

    suspend fun updateRecurring(recurringTransaction: RecurringTransactionEntity) {
        dao.update(recurringTransaction)
    }
}