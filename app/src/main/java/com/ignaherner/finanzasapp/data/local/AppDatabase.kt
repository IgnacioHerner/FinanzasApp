package com.ignaherner.finanzasapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ignaherner.finanzasapp.model.converters.Converters

@Database(
    entities = [
        TransactionEntity::class,
        RecurringTransactionEntity::class,
        CreditCardEntity::class,
        CreditTransactionEntity::class
    ], version = 5, exportSchema = false
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
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }

}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar columna currency a la tabla transactions
        database.execSQL("ALTER TABLE transactions ADD COLUMN currency TEXT NOT NULL DEFAULT 'ARS'")

        // Agregar columna currency a la tabla recurring_transactions
        database.execSQL("ALTER TABLE recurring_transactions ADD COLUMN currency TEXT NOT NULL DEFAULT 'ARS'")
    }
}

