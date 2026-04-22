package com.example.catatankeuanganpribadi.data.local.model

import androidx.room.ColumnInfo

data class TransactionWithDetails(
    val id: Long,
    val type: FinanceType,
    val amount: Long,
    @ColumnInfo(name = "date_time")
    val dateTime: Long,
    val note: String?,
    @ColumnInfo(name = "account_id")
    val accountId: Long,
    @ColumnInfo(name = "account_name")
    val accountName: String,
    @ColumnInfo(name = "account_type")
    val accountType: AccountType,
    @ColumnInfo(name = "transfer_account_id")
    val transferAccountId: Long?,
    @ColumnInfo(name = "transfer_account_name")
    val transferAccountName: String?,
    @ColumnInfo(name = "category_id")
    val categoryId: Long?,
    @ColumnInfo(name = "category_name")
    val categoryName: String?,
    @ColumnInfo(name = "category_icon")
    val categoryIcon: String?,
    @ColumnInfo(name = "category_color_hex")
    val categoryColorHex: String?
)
