package com.example.catatankeuanganpribadi.presentation.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ManageCategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true
)

class ManageCategoriesViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageCategoriesUiState())
    val uiState: StateFlow<ManageCategoriesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.observeCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories, isLoading = false) }
            }
        }
    }

    fun saveCategory(id: Long, name: String, type: TransactionType) {
        viewModelScope.launch {
            val existingCategory = if (id != 0L) categoryRepository.getCategory(id) else null
            val category = Category(
                id = id,
                name = name,
                type = type,
                icon = existingCategory?.icon ?: "category",
                colorHex = existingCategory?.colorHex ?: "#6C63FF",
                parentCategoryId = existingCategory?.parentCategoryId,
                isDefault = existingCategory?.isDefault ?: false
            )
            categoryRepository.saveCategory(category)
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            val category = categoryRepository.getCategory(categoryId)
            if (category?.isDefault == true) return@launch
            categoryRepository.deleteCategory(categoryId)
        }
    }
}
