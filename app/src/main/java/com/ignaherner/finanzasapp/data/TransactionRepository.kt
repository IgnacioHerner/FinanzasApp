package com.ignaherner.finanzasapp.data

import com.ignaherner.finanzasapp.data.local.TransactionDao
import com.ignaherner.finanzasapp.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {

    val allTransaction: Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun insertTransaction(transaction: TransactionEntity) {
        dao.insert(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        dao.delete(transaction)
    }
}