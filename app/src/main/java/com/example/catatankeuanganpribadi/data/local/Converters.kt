package com.example.catatankeuanganpribadi.data.local

import androidx.room.TypeConverter
import com.example.catatankeuanganpribadi.data.local.model.AccountType
import com.example.catatankeuanganpribadi.data.local.model.BudgetPeriod
import com.example.catatankeuanganpribadi.data.local.model.FinanceType

class Converters {

    @TypeConverter
    fun fromFinanceType(value: FinanceType): String = value.name

    @TypeConverter
    fun toFinanceType(value: String): FinanceType = FinanceType.valueOf(value)

    @TypeConverter
    fun fromAccountType(value: AccountType): String = value.name

    @TypeConverter
    fun toAccountType(value: String): AccountType = AccountType.valueOf(value)

    @TypeConverter
    fun fromBudgetPeriod(value: BudgetPeriod): String = value.name

    @TypeConverter
    fun toBudgetPeriod(value: String): BudgetPeriod = BudgetPeriod.valueOf(value)
}
