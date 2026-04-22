package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository

class ObserveTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(dateRange: DateRange) = transactionRepository.observeTransactions(dateRange)
}
