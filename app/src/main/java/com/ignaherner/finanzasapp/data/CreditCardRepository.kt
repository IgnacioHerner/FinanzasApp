package com.ignaherner.finanzasapp.data

import com.ignaherner.finanzasapp.data.local.CreditCardDao
import com.ignaherner.finanzasapp.data.local.CreditCardEntity
import kotlinx.coroutines.flow.Flow

class CreditCardRepository(private val dao: CreditCardDao) {

    val cards: Flow<List<CreditCardEntity>> = dao.getAll()

    suspend fun insert(card: CreditCardEntity) = dao.insert(card)

    suspend fun update(card: CreditCardEntity) = dao.update(card)

    suspend fun delete(card: CreditCardEntity) = dao.delete(card)

    suspend fun getById(id: Int): CreditCardEntity? = dao.getById(id)

}