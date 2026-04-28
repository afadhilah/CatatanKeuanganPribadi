# Arsitektur Aplikasi

CatatanKeuanganPribadi menggunakan **Clean Architecture** yang dikombinasikan dengan **MVVM (Model-View-ViewModel)** pattern.

---

## Prinsip Clean Architecture

Clean Architecture membagi aplikasi menjadi beberapa layer dengan dependency yang jelas:

```
┌─────────────────────────────────────────────────┐
│            Presentation Layer (UI)              │
│         (Composable, ViewModel, State)          │
└─────────────────────────────────────────────────┘
                      ↓ ↑
┌─────────────────────────────────────────────────┐
│            Domain Layer (Business Logic)         │
│          (UseCase, Repository Interface)        │
└─────────────────────────────────────────────────┘
                      ↓ ↑
┌─────────────────────────────────────────────────┐
│            Data Layer (Database & API)          │
│      (Local Database, Repository Implementation)│
└─────────────────────────────────────────────────┘
```

---

## Layer Breakdown

### 1. **Presentation Layer** (UI)

Bertanggung jawab untuk menampilkan UI dan menangani user interaction.

**Komponen:**
- **Composable** - UI components (Jetpack Compose)
- **ViewModel** - Mengelola state dan business logic untuk UI
- **Screen/Page** - Container untuk navigasi
- **Event** - User interaction event

**Technology:**
- Jetpack Compose
- LiveData / StateFlow
- Navigation Component

**Contoh:**
```
app/src/main/java/com/example/catatankeuanganpribadi/ui/
├── screen/
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   ├── transaction/
│   │   ├── TransactionListScreen.kt
│   │   ├── TransactionDetailScreen.kt
│   │   └── TransactionViewModel.kt
│   └── budget/
│       ├── BudgetScreen.kt
│       └── BudgetViewModel.kt
├── component/
│   ├── TransactionCard.kt
│   ├── BudgetCard.kt
│   └── CustomChart.kt
└── navigation/
    └── NavGraph.kt
```

---

### 2. **Domain Layer** (Business Logic)

Layer yang berisi business logic dan tidak tergantung pada framework.

**Komponen:**
- **UseCase** - Encapsulate business logic
- **Entity** - Model domain (berbeda dengan database model)
- **Repository Interface** - Contract untuk data access

**Contoh:**
```
app/src/main/java/com/example/catatankeuanganpribadi/domain/
├── usecase/
│   ├── transaction/
│   │   ├── GetAllTransactionsUseCase.kt
│   │   ├── CreateTransactionUseCase.kt
│   │   └── DeleteTransactionUseCase.kt
│   ├── budget/
│   │   ├── GetBudgetUseCase.kt
│   │   └── SetBudgetUseCase.kt
│   └── account/
│       ├── GetAccountsUseCase.kt
│       └── CreateAccountUseCase.kt
├── entity/
│   ├── Transaction.kt
│   ├── Account.kt
│   ├── Budget.kt
│   └── Category.kt
└── repository/
    ├── TransactionRepository.kt
    ├── AccountRepository.kt
    └── BudgetRepository.kt
```

---

### 3. **Data Layer** (Database & API)

Layer yang menangani penyimpanan dan pengambilan data.

**Komponen:**
- **Local Database** - Room database untuk offline storage
- **Repository Implementation** - Implementasi interface dari domain layer
- **DAO** - Data Access Object untuk database queries
- **Entity (Database)** - Database models (berbeda dari domain entity)

**Contoh:**
```
app/src/main/java/com/example/catatankeuanganpribadi/data/
├── local/
│   ├── database/
│   │   ├── AppDatabase.kt
│   │   └── AppDatabaseMigration.kt
│   ├── dao/
│   │   ├── TransactionDao.kt
│   │   ├── AccountDao.kt
│   │   └── BudgetDao.kt
│   └── entity/
│       ├── TransactionEntity.kt
│       ├── AccountEntity.kt
│       ├── BudgetEntity.kt
│       └── CategoryEntity.kt
├── repository/
│   ├── TransactionRepositoryImpl.kt
│   ├── AccountRepositoryImpl.kt
│   └── BudgetRepositoryImpl.kt
└── mapper/
    ├── TransactionMapper.kt (Entity ↔ Domain)
    └── AccountMapper.kt
```

---

## Dependency Injection

Project menggunakan **Dependency Injection** untuk loose coupling.

**Framework:** Hilt / Manual DI

```kotlin
// Hilt Module (jika menggunakan Hilt)
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "catatan_keuangan.db"
        ).build()
    }
}
```

---

## Data Flow

### Transaksi Data (Unidirectional):

```
User Action (Button Click)
    ↓
ViewModel Event Handler
    ↓
UseCase Execute
    ↓
Repository Query
    ↓
Database/API
    ↓
Repository Return Data
    ↓
UseCase Transform Data
    ↓
ViewModel Update State (StateFlow/LiveData)
    ↓
Composable Recompose with new State
    ↓
UI Update
```

---

## Teknologi yang Digunakan

### UI Framework:
- **Jetpack Compose** - Modern UI toolkit

### Architecture:
- **MVVM Pattern** - Separation of concerns
- **Clean Architecture** - Layer separation

### Database:
- **Room** - SQLite abstraction
- **KSP** - Kotlin Symbol Processing (annotation processor)

### Concurrency:
- **Coroutines** - Async programming
- **Flow** - Reactive streams

### Dependency Injection:
- **Hilt** atau Manual DI

### Testing:
- **JUnit 4** - Unit testing
- **Mockito** - Mocking library
- **Espresso** - UI testing

---

## Module Dependencies

```
app (main application module)
├── (depends on)
├── data layer
│   └── (depends on) domain
└── presentation layer
    └── (depends on) domain

domain (business logic - no Android dependency)
├── entities
├── repositories (interfaces)
└── usecases
```

---

## Best Practices

1. **Separation of Concerns** - Setiap layer punya tanggung jawab sendiri
2. **Unidirectional Data Flow** - Data mengalir satu arah
3. **Testability** - Business logic mudah untuk ditest
4. **Reusability** - Component dapat digunakan kembali
5. **Maintainability** - Code mudah untuk di-maintain

---

## Resource Tambahan

- [Clean Architecture Guide](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Android Architecture Patterns](https://developer.android.com/topic/architecture)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**Last Updated**: April 29, 2026
