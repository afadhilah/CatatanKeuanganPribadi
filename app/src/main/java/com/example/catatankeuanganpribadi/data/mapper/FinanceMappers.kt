package com.example.catatankeuanganpribadi.data.mapper

import com.example.catatankeuanganpribadi.data.local.entity.AccountEntity
import com.example.catatankeuanganpribadi.data.local.entity.BudgetEntity
import com.example.catatankeuanganpribadi.data.local.entity.CategoryEntity
import com.example.catatankeuanganpribadi.data.local.entity.TransactionEntity
import com.example.catatankeuanganpribadi.data.local.model.BudgetProgress
import com.example.catatankeuanganpribadi.data.local.model.TransactionWithDetails
import com.example.catatankeuanganpribadi.domain.model.Account
import com.example.catatankeuanganpribadi.domain.model.Budget
import com.example.catatankeuanganpribadi.domain.model.BudgetPeriod
import com.example.catatankeuanganpribadi.domain.model.BudgetStatus
import com.example.catatankeuanganpribadi.domain.model.BudgetUsage
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.SaveTransactionRequest
import com.example.catatankeuanganpribadi.domain.model.Transaction
import com.example.catatankeuanganpribadi.domain.model.TransactionDetails

fun AccountEntity.toDomain(): Account = Account(
    id = id,
    name = name,
    type = type.toDomain(),
    balance = balance,
    createdAt = createdAt
)

fun Account.toEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    type = type.toLocal(),
    balance = balance,
    createdAt = createdAt
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    type = type.toDomain(),
    icon = icon,
    colorHex = colorHex,
    parentCategoryId = parentCategoryId,
    isDefault = isDefault
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    type = type.toLocal(),
    icon = icon,
    colorHex = colorHex,
    parentCategoryId = parentCategoryId,
    isDefault = isDefault
)

fun BudgetEntity.toDomain(): Budget = Budget(
    id = id,
    categoryId = categoryId,
    limitAmount = limitAmount,
    period = period.toDomain()
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    categoryId = categoryId,
    limitAmount = limitAmount,
    period = period.toLocal()
)

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    type = type.toDomain(),
    amount = amount,
    accountId = accountId,
    transferAccountId = transferAccountId,
    categoryId = categoryId,
    dateTime = dateTime,
    note = note
)

fun SaveTransactionRequest.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    type = type.toLocal(),
    amount = amount,
    accountId = accountId,
    transferAccountId = transferAccountId,
    categoryId = categoryId,
    dateTime = dateTime,
    note = note
)

fun TransactionWithDetails.toDomain(): TransactionDetails = TransactionDetails(
    id = id,
    type = type.toDomain(),
    amount = amount,
    dateTime = dateTime,
    note = note,
    accountId = accountId,
    accountName = accountName,
    accountType = accountType.toDomain(),
    transferAccountId = transferAccountId,
    transferAccountName = transferAccountName,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryIcon = categoryIcon,
    categoryColorHex = categoryColorHex
)

fun BudgetProgress.toDomain(): BudgetUsage {
    val progressValue = if (limitAmount == 0L) 0f else usedAmount.toFloat() / limitAmount.toFloat()
    val status = when {
        progressValue >= 1f -> BudgetStatus.EXCEEDED
        progressValue >= 0.8f -> BudgetStatus.WARNING
        else -> BudgetStatus.SAFE
    }

    return BudgetUsage(
        budgetId = budgetId,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryIcon = categoryIcon,
        categoryColorHex = categoryColorHex,
        limitAmount = limitAmount,
        usedAmount = usedAmount,
        progress = progressValue,
        status = status
    )
}

private fun com.example.catatankeuanganpribadi.data.local.model.AccountType.toDomain() =
    when (this) {
        com.example.catatankeuanganpribadi.data.local.model.AccountType.CASH -> com.example.catatankeuanganpribadi.domain.model.AccountType.CASH
        com.example.catatankeuanganpribadi.data.local.model.AccountType.BANK -> com.example.catatankeuanganpribadi.domain.model.AccountType.BANK
        com.example.catatankeuanganpribadi.data.local.model.AccountType.E_WALLET -> com.example.catatankeuanganpribadi.domain.model.AccountType.E_WALLET
    }

private fun com.example.catatankeuanganpribadi.domain.model.AccountType.toLocal() =
    when (this) {
        com.example.catatankeuanganpribadi.domain.model.AccountType.CASH -> com.example.catatankeuanganpribadi.data.local.model.AccountType.CASH
        com.example.catatankeuanganpribadi.domain.model.AccountType.BANK -> com.example.catatankeuanganpribadi.data.local.model.AccountType.BANK
        com.example.catatankeuanganpribadi.domain.model.AccountType.E_WALLET -> com.example.catatankeuanganpribadi.data.local.model.AccountType.E_WALLET
    }

private fun com.example.catatankeuanganpribadi.data.local.model.FinanceType.toDomain() =
    when (this) {
        com.example.catatankeuanganpribadi.data.local.model.FinanceType.INCOME -> com.example.catatankeuanganpribadi.domain.model.TransactionType.INCOME
        com.example.catatankeuanganpribadi.data.local.model.FinanceType.EXPENSE -> com.example.catatankeuanganpribadi.domain.model.TransactionType.EXPENSE
        com.example.catatankeuanganpribadi.data.local.model.FinanceType.TRANSFER -> com.example.catatankeuanganpribadi.domain.model.TransactionType.TRANSFER
    }

private fun com.example.catatankeuanganpribadi.domain.model.TransactionType.toLocal() =
    when (this) {
        com.example.catatankeuanganpribadi.domain.model.TransactionType.INCOME -> com.example.catatankeuanganpribadi.data.local.model.FinanceType.INCOME
        com.example.catatankeuanganpribadi.domain.model.TransactionType.EXPENSE -> com.example.catatankeuanganpribadi.data.local.model.FinanceType.EXPENSE
        com.example.catatankeuanganpribadi.domain.model.TransactionType.TRANSFER -> com.example.catatankeuanganpribadi.data.local.model.FinanceType.TRANSFER
    }

private fun com.example.catatankeuanganpribadi.data.local.model.BudgetPeriod.toDomain() =
    when (this) {
        com.example.catatankeuanganpribadi.data.local.model.BudgetPeriod.MONTHLY -> BudgetPeriod.MONTHLY
    }

private fun BudgetPeriod.toLocal() =
    when (this) {
        BudgetPeriod.MONTHLY -> com.example.catatankeuanganpribadi.data.local.model.BudgetPeriod.MONTHLY
    }
