package com.ignaherner.finanzasapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ignaherner.finanzasapp.model.converters.Converters

@Database(
    entities = [
        TransactionEntity::class,
        RecurringTransactionEntity::class,
        CreditCardEntity::class,
        CreditTransactionEntity::class
    ], version = 2, exportSchema = false
)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun recurringTransactionDao(): RecurringTransactionDao

    abstract fun creditCardDao(): CreditCardDao

    abstract fun creditTransactionDao(): CreditTransactionDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finanzas_db"
                )
                    .build().also { INSTANCE = it }
            }
    }
}
