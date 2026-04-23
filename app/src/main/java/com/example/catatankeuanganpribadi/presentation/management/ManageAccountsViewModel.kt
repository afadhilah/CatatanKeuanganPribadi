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
    val isLoading: Boolean = true
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

    fun saveAccount(id: Long, name: String, balance: Long) {
        viewModelScope.launch {
            val account = Account(
                id = id,
                name = name,
                type = AccountType.CASH, // Default for now
                balance = balance,
                createdAt = System.currentTimeMillis()
            )
            accountRepository.saveAccount(account)
        }
    }

    fun deleteAccount(accountId: Long) {
        viewModelScope.launch {
            accountRepository.deleteAccount(accountId)
        }
    }
}
