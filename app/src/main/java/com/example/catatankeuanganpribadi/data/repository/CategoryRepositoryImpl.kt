package com.example.catatankeuanganpribadi.data.repository

import com.example.catatankeuanganpribadi.data.local.dao.CategoryDao
import com.example.catatankeuanganpribadi.data.local.seed.DefaultCategories
import com.example.catatankeuanganpribadi.data.mapper.toDomain
import com.example.catatankeuanganpribadi.data.mapper.toEntity
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun observeCategories(): Flow<List<Category>> {
        return categoryDao.observeCategories().map { categories -> categories.map { it.toDomain() } }
    }

    override fun observeCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.observeCategoriesByType(type.toLocal())
            .map { categories -> categories.map { it.toDomain() } }
    }

    override suspend fun getCategory(categoryId: Long): Category? {
        return categoryDao.getCategoryById(categoryId)?.toDomain()
    }

    override suspend fun seedDefaultCategoriesIfNeeded() {
        if (categoryDao.getCategoryCount() == 0) {
            DefaultCategories.items.forEach { categoryDao.upsertCategory(it) }
        }
    }

    override suspend fun saveCategory(category: Category): Long {
        return categoryDao.upsertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(categoryId: Long) {
        val category = categoryDao.getCategoryById(categoryId)
        if (category != null && !category.isDefault) {
            categoryDao.deleteCategory(category)
        }
    }

    private fun TransactionType.toLocal() =
        when (this) {
            TransactionType.INCOME -> com.example.catatankeuanganpribadi.data.local.model.FinanceType.INCOME
            TransactionType.EXPENSE -> com.example.catatankeuanganpribadi.data.local.model.FinanceType.EXPENSE
            TransactionType.TRANSFER -> com.example.catatankeuanganpribadi.data.local.model.FinanceType.TRANSFER
        }
}
