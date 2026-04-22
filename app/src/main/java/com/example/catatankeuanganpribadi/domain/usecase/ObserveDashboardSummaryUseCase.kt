package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository

class ObserveDashboardSummaryUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(dateRange: DateRange) = transactionRepository.observeDashboardSummary(dateRange)
}
