package com.ignaherner.finanzasapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tx: CreditTransactionEntity)

    @Update
    suspend fun update(tx: CreditTransactionEntity)

    @Delete
    suspend fun delete(tx: CreditTransactionEntity)

    @Query("SELECT * FROM credit_transactions WHERE cardId = :cardId ORDER BY date DESC")
    fun getTransactionsByCard(cardId: Int): Flow<List<CreditTransactionEntity>>

    @Query("SELECT * FROM credit_transactions ORDER BY date DESC")
    fun getAll(): Flow<List<CreditTransactionEntity>>

    @Query("SELECT cardId, SUM(amount) AS used FROM credit_transactions GROUP BY cardId")
    fun getUsage(): Flow<List<CardUsage>>


}