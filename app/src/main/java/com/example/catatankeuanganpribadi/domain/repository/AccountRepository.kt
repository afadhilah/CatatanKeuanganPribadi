package com.example.catatankeuanganpribadi.domain.repository

import com.example.catatankeuanganpribadi.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccounts(): Flow<List<Account>>
    fun observeTotalBalance(): Flow<Long>
    suspend fun getAccount(accountId: Long): Account?
    suspend fun saveAccount(account: Account): Long
    suspend fun deleteAccount(accountId: Long)
}
