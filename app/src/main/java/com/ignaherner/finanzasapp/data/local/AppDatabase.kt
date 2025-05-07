package com.ignaherner.finanzasapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ignaherner.finanzasapp.model.converters.Converters

@Database(entities = [TransactionEntity::class, RecurringTransactionEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun recurringTransactionDao(): RecurringTransactionDao
    

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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build().also { INSTANCE = it }
            }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE transactions ADD COLUMN isPaid INTEGER NOT NULL DEFAULT 1")
    }
}

// Migra de la versión 2 a la 3, creando la nueva tabla credit_cards
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
      CREATE TABLE IF NOT EXISTS `credit_cards` (
        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        `name` TEXT NOT NULL,
        `limit` REAL NOT NULL,
        `dueDate` INTEGER NOT NULL,
        `cutDate` INTEGER NOT NULL
      )
    """.trimIndent())
    }
}
/** Migra de la versión 3 a la 4 creando la tabla credit_transactions */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `credit_transactions` (
              `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
              `cardId` INTEGER NOT NULL,
              `title` TEXT NOT NULL,
              `amount` REAL NOT NULL,
              `date` TEXT NOT NULL,
              `isPaid` INTEGER NOT NULL DEFAULT 1
            )
        """.trimIndent()
        )
    }
}