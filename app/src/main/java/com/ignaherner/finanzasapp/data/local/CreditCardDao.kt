package com.ignaherner.finanzasapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(creditCard: CreditCardEntity)

    @Update
    suspend fun update(creditCard: CreditCardEntity)

    @Delete
    suspend fun delete(creditCard: CreditCardEntity)

    @Query("SELECT * FROM credit_cards ORDER BY name ASC")
    fun getAll(): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): CreditCardEntity?
}