package com.ignaherner.finanzasapp.model.converters

import androidx.room.TypeConverter
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.model.RecurrenceType
import com.ignaherner.finanzasapp.model.TransactionType
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromTransactionType(value: TransactionType) : String = value.name

    @TypeConverter
    fun toTransactionType(value: String) : TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun fromAccount(value: Account) : String = value.name

    @TypeConverter
    fun toAccount(value: String) : Account = Account.valueOf(value)

    @TypeConverter
    fun fromCategory(value: Category) : String = value.name

    @TypeConverter
    fun toCategory(value: String) : Category = Category.valueOf(value)

    @TypeConverter
    fun fromLocalDate(date: LocalDate) : String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromRecurrenceType(value: RecurrenceType) : String = value.name

    @TypeConverter
    fun toRecurrenceType(value: String) : RecurrenceType = RecurrenceType.valueOf(value)
}