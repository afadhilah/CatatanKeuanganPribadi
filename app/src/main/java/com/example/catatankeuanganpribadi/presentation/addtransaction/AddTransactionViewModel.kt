package com.example.catatankeuanganpribadi.presentation.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.*
import com.example.catatankeuanganpribadi.domain.repository.AccountRepository
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository
import com.example.catatankeuanganpribadi.domain.usecase.SaveTransactionUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val transactionId: Long,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    private val isEditMode: Boolean = transactionId != -1L

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.seedDefaultCategoriesIfNeeded()
            seedDefaultAccountIfNeeded()

            // Start observing data after seeding
            launch { observeAccounts() }
            launch { observeCategories() }

            if (transactionId != -1L) {
                loadTransaction(transactionId)
            }
        }
    }

    private suspend fun loadTransaction(id: Long) {
        val transaction = transactionRepository.getTransaction(id) ?: return
        _uiState.update { state ->
            state.copy(
                selectedType = transaction.type,
                amountInput = transaction.amount.toString(),
                selectedAccountId = transaction.accountId,
                selectedTransferAccountId = transaction.transferAccountId,
                selectedCategoryId = transaction.categoryId,
                dateTime = transaction.dateTime,
                note = transaction.note.orEmpty(),
                isLoading = false
            )
        }
    }

    private suspend fun observeAccounts() {
        accountRepository.observeAccounts().collect { accounts ->
            _uiState.update { state ->
                val validatedSelectedAccountId = state.selectedAccountId
                    ?.takeIf { selectedId -> accounts.any { it.id == selectedId } }
                val fallbackAccountId = if (isEditMode) accounts.firstOrNull()?.id else null
                val nextSelectedAccountId = validatedSelectedAccountId ?: fallbackAccountId

                val validatedTransferAccountId = state.selectedTransferAccountId
                    ?.takeIf { transferId ->
                        accounts.any { it.id == transferId } && transferId != nextSelectedAccountId
                    }

                state.copy(
                    accounts = accounts,
                    selectedAccountId = nextSelectedAccountId,
                    selectedTransferAccountId = if (state.selectedType == TransactionType.TRANSFER) {
                        validatedTransferAccountId
                    } else {
                        null
                    },
                    isLoading = false
                )
            }
        }
    }

    private suspend fun observeCategories() {
        _uiState.map { it.selectedType }
            .distinctUntilChanged()
            .flatMapLatest { type ->
                categoryRepository.observeCategoriesByType(type)
            }
            .collect { categories ->
                _uiState.update { state ->
                    val validatedSelectedCategoryId = state.selectedCategoryId
                        ?.takeIf { selectedId -> categories.any { it.id == selectedId } }

                    state.copy(
                        categories = categories,
                        selectedCategoryId = if (state.selectedType == TransactionType.TRANSFER) {
                            null
                        } else {
                            validatedSelectedCategoryId ?: if (isEditMode) {
                                categories.firstOrNull()?.id
                            } else {
                                null
                            }
                        }
                    )
                }
            }
    }

    private suspend fun seedDefaultAccountIfNeeded() {
        if (accountRepository.observeAccounts().first().isNotEmpty()) return

        accountRepository.saveAccount(
            Account(
                id = 0L,
                name = "Cash",
                type = AccountType.CASH,
                balance = 0L,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    fun updateType(type: TransactionType) {
        _uiState.update {
            it.copy(
                selectedType = type,
                selectedCategoryId = if (type == TransactionType.TRANSFER) null else it.selectedCategoryId,
                selectedTransferAccountId = if (type == TransactionType.TRANSFER) it.selectedTransferAccountId else null,
                errorMessage = null,
                saveCompleted = false
            )
        }
    }

    fun updateAmount(value: String) {
        val filtered = value.filter(Char::isDigit)
        _uiState.update { it.copy(amountInput = filtered, errorMessage = null) }
    }

    fun updateAccount(accountId: Long) {
        _uiState.update { it.copy(selectedAccountId = accountId, errorMessage = null) }
    }

    fun updateTransferAccount(accountId: Long?) {
        _uiState.update { it.copy(selectedTransferAccountId = accountId, errorMessage = null) }
    }

    fun updateCategory(categoryId: Long?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId, errorMessage = null) }
    }

    fun updateDateTime(dateTime: Long) {
        _uiState.update { it.copy(dateTime = dateTime) }
    }

    fun updateNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val currentState = uiState.value
            val amount = currentState.amountInput.toLongOrNull()

            if (amount == null || amount <= 0L) {
                _uiState.update { it.copy(errorMessage = "Nominal harus lebih besar dari 0") }
                return@launch
            }

            val accountId = currentState.selectedAccountId
            if (accountId == null) {
                _uiState.update { it.copy(errorMessage = "Pilih akun terlebih dahulu") }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true, errorMessage = null, saveCompleted = false) }

            runCatching {
                saveTransactionUseCase(
                    SaveTransactionRequest(
                        id = if (transactionId != -1L) transactionId else 0L,
                        type = currentState.selectedType,
                        amount = amount,
                        accountId = accountId,
                        transferAccountId = currentState.selectedTransferAccountId,
                        categoryId = currentState.selectedCategoryId,
                        dateTime = currentState.dateTime,
                        note = currentState.note.ifBlank { null }
                    )
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveCompleted = true
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Gagal menyimpan transaksi"
                    )
                }
            }
        }
    }

    fun consumeSaveResult() {
        _uiState.update { it.copy(saveCompleted = false) }
    }
}
