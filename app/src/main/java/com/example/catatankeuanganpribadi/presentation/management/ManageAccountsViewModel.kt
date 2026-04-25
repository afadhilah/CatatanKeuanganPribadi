package com.example.catatankeuanganpribadi.presentation.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.Account
import com.example.catatankeuanganpribadi.domain.model.AccountType
import com.example.catatankeuanganpribadi.domain.repository.AccountRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ManageAccountsUiState(
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ManageAccountsViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageAccountsUiState())
    val uiState: StateFlow<ManageAccountsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            accountRepository.observeAccounts().collect { accounts ->
                _uiState.update { it.copy(accounts = accounts, isLoading = false) }
            }
        }
    }

    fun saveAccount(id: Long, name: String) {
        viewModelScope.launch {
            runCatching {
                val existingAccount = if (id == 0L) null else accountRepository.getAccount(id)
                val account = if (existingAccount != null) {
                    existingAccount.copy(name = name)
                } else {
                    Account(
                        id = 0L,
                        name = name,
                        type = AccountType.CASH,
                        balance = 0L,
                        createdAt = System.currentTimeMillis()
                    )
                }
                accountRepository.saveAccount(account)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message ?: "Gagal menyimpan akun")
                }
            }
        }
    }

    fun deleteAccount(accountId: Long) {
        viewModelScope.launch {
            runCatching {
                accountRepository.deleteAccount(accountId)
            }.onFailure { throwable ->
                val fallbackMessage = if (throwable.message.orEmpty().contains("FOREIGN KEY", ignoreCase = true)) {
                    "Akun tidak bisa dihapus karena masih dipakai oleh transaksi"
                } else {
                    throwable.message ?: "Gagal menghapus akun"
                }
                _uiState.update { it.copy(errorMessage = fallbackMessage) }
            }
        }
    }

    fun consumeErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
