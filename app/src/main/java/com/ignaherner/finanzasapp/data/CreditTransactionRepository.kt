package com.ignaherner.finanzasapp.data

import com.ignaherner.finanzasapp.data.local.CardUsage
import com.ignaherner.finanzasapp.data.local.CreditTransactionDao
import com.ignaherner.finanzasapp.data.local.CreditTransactionEntity
import kotlinx.coroutines.flow.Flow

class CreditTransactionRepository(private val dao: CreditTransactionDao) {

    fun getAll(): Flow<List<CreditTransactionEntity>> = dao.getAll()

    fun getByCard(cardId: Int): Flow<List<CreditTransactionEntity>> = dao.getTransactionsByCard(cardId)

    fun getUsage(): Flow<List<CardUsage>> = dao.getUsage()


    suspend fun insert(tx: CreditTransactionEntity) = dao.insert(tx)

    suspend fun update(tx: CreditTransactionEntity) = dao.update(tx)

    suspend fun delete(tx: CreditTransactionEntity) = dao.delete(tx)
}