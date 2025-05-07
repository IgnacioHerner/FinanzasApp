package com.ignaherner.finanzasapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(recurring: RecurringTransactionEntity)

    @Update
    suspend fun update(recurring: RecurringTransactionEntity)

    @Delete
    suspend fun delete(recurring: RecurringTransactionEntity)

    @Query("SELECT * FROM recurring_transactions ORDER BY startDate ASC")
    fun getAllRecurring(): Flow<List<RecurringTransactionEntity>>
}