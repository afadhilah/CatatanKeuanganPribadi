package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.domain.repository.BudgetRepository

class ObserveBudgetUsageUseCase(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(dateRange: DateRange) = budgetRepository.observeBudgetUsage(dateRange)
}
