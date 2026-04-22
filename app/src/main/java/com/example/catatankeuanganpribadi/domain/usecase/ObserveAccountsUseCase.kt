package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.repository.AccountRepository

class ObserveAccountsUseCase(
    private val accountRepository: AccountRepository
) {
    operator fun invoke() = accountRepository.observeAccounts()
}
