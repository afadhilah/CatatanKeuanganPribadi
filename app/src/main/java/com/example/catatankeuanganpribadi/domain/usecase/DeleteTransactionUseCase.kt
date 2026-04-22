package com.example.catatankeuanganpribadi.domain.usecase

import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Long) {
        transactionRepository.deleteTransaction(transactionId)
    }
}
