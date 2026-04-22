package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository

class ObserveRecentTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(limit: Int = 10) = transactionRepository.observeRecentTransactions(limit)
}
