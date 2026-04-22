package com.example.catatankeuanganpribadi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.catatankeuanganpribadi.data.local.model.AccountType

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: AccountType,
    val balance: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
