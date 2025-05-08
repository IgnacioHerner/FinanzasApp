package com.ignaherner.finanzasapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val limit: Double,
    val dueDate: Int,
    val cutDate: Int
)
