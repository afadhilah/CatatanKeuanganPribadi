package com.example.catatankeuanganpribadi.data.repository

import com.example.catatankeuanganpribadi.data.local.dao.AccountDao
import com.example.catatankeuanganpribadi.data.mapper.toDomain
import com.example.catatankeuanganpribadi.data.mapper.toEntity
import com.example.catatankeuanganpribadi.domain.model.Account
import com.example.catatankeuanganpribadi.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun observeAccounts(): Flow<List<Account>> {
        return accountDao.observeAccounts().map { accounts -> accounts.map { it.toDomain() } }
    }

    override fun observeTotalBalance(): Flow<Long> = accountDao.observeTotalBalance()

    override suspend fun getAccount(accountId: Long): Account? {
        return accountDao.getAccountById(accountId)?.toDomain()
    }

    override suspend fun saveAccount(account: Account): Long {
        return accountDao.upsertAccount(account.toEntity())
    }

    override suspend fun deleteAccount(accountId: Long) {
        accountDao.getAccountById(accountId)?.let { account ->
            accountDao.deleteAccount(account)
        }
    }
}