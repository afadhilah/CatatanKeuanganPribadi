package com.example.catatankeuanganpribadi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.catatankeuanganpribadi.data.local.dao.AccountDao
import com.example.catatankeuanganpribadi.data.local.dao.BudgetDao
import com.example.catatankeuanganpribadi.data.local.dao.CategoryDao
import com.example.catatankeuanganpribadi.data.local.dao.TransactionDao
import com.example.catatankeuanganpribadi.data.local.entity.AccountEntity
import com.example.catatankeuanganpribadi.data.local.entity.BudgetEntity
import com.example.catatankeuanganpribadi.data.local.entity.CategoryEntity
import com.example.catatankeuanganpribadi.data.local.entity.TransactionEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { database ->
                    INSTANCE = database
                }
            }
        }

        private const val DATABASE_NAME = "finance.db"
    }
}
