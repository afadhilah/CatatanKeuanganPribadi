package com.example.catatankeuanganpribadi.domain.repository

import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeCategories(): Flow<List<Category>>
    fun observeCategoriesByType(type: TransactionType): Flow<List<Category>>
    suspend fun getCategory(categoryId: Long): Category?
    suspend fun seedDefaultCategoriesIfNeeded()
    suspend fun saveCategory(category: Category): Long
    suspend fun deleteCategory(categoryId: Long)
}
