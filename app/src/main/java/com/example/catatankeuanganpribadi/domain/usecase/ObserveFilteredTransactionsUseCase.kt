package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.model.TransactionFilter
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository

class ObserveFilteredTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(filter: TransactionFilter) = transactionRepository.observeFilteredTransactions(filter)
}
