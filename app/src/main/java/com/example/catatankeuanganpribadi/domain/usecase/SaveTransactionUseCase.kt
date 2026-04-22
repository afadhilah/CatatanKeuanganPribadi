package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.model.SaveTransactionRequest
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository

class SaveTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(request: SaveTransactionRequest): Long {
        return transactionRepository.saveTransaction(request)
    }
}
