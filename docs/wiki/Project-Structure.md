# Project Structure

Penjelasan lengkap struktur folder dan file dalam project CatatanKeuanganPribadi.

---

## Root Level

```
CatatanKeuanganPribadi/
├── app/                           # Main application module
├── gradle/                        # Gradle configuration
├── .github/                       # GitHub configuration
├── docs/                          # Documentation
├── build.gradle.kts               # Root build configuration
├── settings.gradle.kts            # Gradle settings
├── gradle.properties              # Gradle properties
├── local.properties               # Local SDK path (gitignored)
├── README.md                      # Project overview
└── [other config files]
```

---

## app/ - Main Module

```
app/
├── src/                          # Source code
│   ├── main/                     # Main source set
│   ├── test/                     # Unit tests
│   └── androidTest/              # Instrumented tests
├── build/                        # Build output (gitignored)
├── schemas/                      # Room database schemas
├── build.gradle.kts              # App build configuration
└── proguard-rules.pro            # ProGuard rules for release build
```

### app/src/main/

```
main/
├── java/
│   └── com/example/catatankeuanganpribadi/
│       ├── ui/                   # Presentation Layer
│       │   ├── screen/           # Screen composables
│       │   ├── component/        # Reusable UI components
│       │   ├── navigation/       # Navigation setup
│       │   └── viewmodel/        # ViewModels
│       ├── domain/               # Domain Layer
│       │   ├── usecase/          # Use cases (business logic)
│       │   ├── entity/           # Domain entities
│       │   └── repository/       # Repository interfaces
│       ├── data/                 # Data Layer
│       │   ├── local/            # Local database
│       │   │   ├── dao/          # Room DAOs
│       │   │   └── entity/       # Database entities
│       │   ├── repository/       # Repository implementations
│       │   └── mapper/           # Entity mappers
│       ├── di/                   # Dependency Injection
│       │   └── Module.kt         # DI modules
│       └── MainActivity.kt       # Entry point
├── res/                          # Resources
│   ├── values/                   # Value resources
│   │   ├── strings.xml           # String constants
│   │   ├── colors.xml            # Color definitions
│   │   ├── dimens.xml            # Dimension values
│   │   └── themes/               # Theme definitions
│   ├── drawable/                 # Drawable resources
│   ├── mipmap/                   # App icons
│   └── layout/                   # XML layouts (if any)
└── AndroidManifest.xml          # App manifest
```

---

## Layer Breakdown

### 1. **UI Layer** (app/src/main/java/.../ui/)

**Purpose**: Presentation logic and UI rendering

```
ui/
├── screen/                       # Full screens
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   ├── DashboardViewModel.kt
│   │   └── DashboardUiState.kt
│   ├── transaction/
│   │   ├── TransactionListScreen.kt
│   │   ├── TransactionDetailScreen.kt
│   │   ├── AddTransactionScreen.kt
│   │   └── TransactionViewModel.kt
│   ├── account/
│   │   ├── AccountScreen.kt
│   │   └── AccountViewModel.kt
│   ├── budget/
│   │   ├── BudgetScreen.kt
│   │   └── BudgetViewModel.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       └── SettingsViewModel.kt
├── component/                    # Reusable components
│   ├── TransactionCard.kt
│   ├── BudgetCard.kt
│   ├── AccountCard.kt
│   ├── Chart.kt
│   └── CustomButton.kt
├── navigation/                   # Navigation setup
│   ├── NavGraph.kt
│   ├── Screen.kt                 # Screen sealed class
│   └── Navigator.kt
└── theme/                        # App theming
    ├── Color.kt
    ├── Typography.kt
    └── Theme.kt
```

**Key Files**:
- `*ViewModel.kt` - MVVM ViewModels
- `*Screen.kt` - Composable screens
- `*UiState.kt` - UI state classes
- `*Component.kt` - Reusable UI components

---

### 2. **Domain Layer** (app/src/main/java/.../domain/)

**Purpose**: Business logic, independent of Android

```
domain/
├── usecase/                      # Use cases (business logic)
│   ├── transaction/
│   │   ├── GetAllTransactionsUseCase.kt
│   │   ├── GetTransactionsByDateUseCase.kt
│   │   ├── CreateTransactionUseCase.kt
│   │   ├── UpdateTransactionUseCase.kt
│   │   └── DeleteTransactionUseCase.kt
│   ├── account/
│   │   ├── GetAccountsUseCase.kt
│   │   ├── CreateAccountUseCase.kt
│   │   └── DeleteAccountUseCase.kt
│   ├── budget/
│   │   ├── GetBudgetUseCase.kt
│   │   ├── SetBudgetUseCase.kt
│   │   └── GetBudgetProgressUseCase.kt
│   └── analytics/
│       ├── GetExpenseAnalyticsUseCase.kt
│       └── GetIncomeAnalyticsUseCase.kt
├── entity/                       # Domain models (not Android)
│   ├── Transaction.kt
│   ├── Account.kt
│   ├── Budget.kt
│   ├── Category.kt
│   └── DashboardSummary.kt
├── repository/                   # Repository interfaces
│   ├── TransactionRepository.kt
│   ├── AccountRepository.kt
│   ├── BudgetRepository.kt
│   └── CategoryRepository.kt
└── common/
    ├── Result.kt                 # Success/Error wrapper
    └── Constants.kt
```

**Key Concepts**:
- No Android dependencies
- Testable business logic
- Repository pattern
- Use cases for specific actions

---

### 3. **Data Layer** (app/src/main/java/.../data/)

**Purpose**: Data access and storage

```
data/
├── local/                        # Local database
│   ├── database/
│   │   ├── AppDatabase.kt        # Room database
│   │   ├── AppDatabaseMigration.kt
│   │   └── Migrations.kt
│   ├── dao/                      # Data Access Objects
│   │   ├── TransactionDao.kt
│   │   ├── AccountDao.kt
│   │   └── BudgetDao.kt
│   ├── entity/                   # Database entities
│   │   ├── TransactionEntity.kt
│   │   ├── AccountEntity.kt
│   │   ├── BudgetEntity.kt
│   │   └── CategoryEntity.kt
│   └── relation/                 # DB relations
│       ├── TransactionWithAccount.kt
│       └── BudgetWithCategory.kt
├── repository/                   # Repository implementations
│   ├── TransactionRepositoryImpl.kt
│   ├── AccountRepositoryImpl.kt
│   ├── BudgetRepositoryImpl.kt
│   └── CategoryRepositoryImpl.kt
├── mapper/                       # Entity to domain mappers
│   ├── TransactionMapper.kt
│   ├── AccountMapper.kt
│   └── BudgetMapper.kt
└── di/                           # Data layer DI
    └── DataModule.kt
```

**Key Components**:
- Room database for SQLite
- DAOs for queries
- Entities for database schema
- Repository implementations
- Mappers for entity conversion

---

### 4. **DI Layer** (app/src/main/java/.../di/)

**Purpose**: Dependency injection setup

```
di/
├── Module.kt                     # Main DI module
├── AppModule.kt                  # App-level dependencies
├── DataModule.kt                 # Data layer dependencies
├── DomainModule.kt               # Domain layer dependencies
└── PresentationModule.kt         # UI layer dependencies
```

---

## gradle/ - Build Configuration

```
gradle/
├── libs.versions.toml            # Version catalog (centralized deps)
└── wrapper/                      # Gradle wrapper
    ├── gradle-wrapper.jar
    ├── gradle-wrapper.properties
    └── gradlew                   # Gradle wrapper script
```

**libs.versions.toml** structure:
```toml
[versions]
kotlin = "2.0.21"
agp = "8.11.2"

[libraries]
androidx-compose = "1.5.0"

[plugins]
android-application = "8.11.2"
kotlin-android = "2.0.21"
```

---

## .github/ - GitHub Configuration

```
.github/
├── workflows/                    # GitHub Actions workflows
│   ├── build-and-release.yml    # Release workflow
│   └── build-debug.yml          # Debug build workflow
└── GITHUB_ACTIONS.md            # Workflows documentation
```

---

## docs/ - Documentation

```
docs/
├── wiki/                         # Wiki pages
│   ├── Home.md                   # Wiki home
│   ├── Features.md               # Features list
│   ├── Installation.md           # User guide
│   ├── Architecture.md           # Architecture docs
│   ├── Development-Setup.md      # Dev setup guide
│   ├── GitHub-Actions.md         # CI/CD documentation
│   ├── Contributing.md           # Contribution guide
│   ├── FAQ.md                    # FAQ
│   └── Project-Structure.md      # This file
└── images/                       # Documentation images
```

---

## Key Configuration Files

### build.gradle.kts (Root)
- Gradle plugins
- Android SDK versions
- Version references

### app/build.gradle.kts
- App-specific build settings
- Dependencies
- Build types (debug, release)
- Product flavors (if any)

### settings.gradle.kts
- Project structure
- Gradle plugin repositories

### gradle.properties
- Gradle JVM args
- Gradle caching
- Build performance tuning

### local.properties
- Android SDK path
- Local configuration (gitignored)

### AndroidManifest.xml
- App declaration
- Permissions
- Activities
- Services

---

## Typical Data Flow

```
MainActivity
    ↓
NavGraph / Navigation
    ↓
Screen (Composable)
    ↓
ViewModel (State Management)
    ↓
UseCase (Business Logic)
    ↓
Repository (Data Abstraction)
    ↓
DAO / Database (Data Persistence)
```

---

## Build Output

```
app/build/
├── generated/                    # Generated code
│   ├── ksp/                     # Kotlin Symbol Processing
│   └── res/
├── intermediates/               # Intermediate files
├── outputs/                     # Build outputs
│   ├── apk/
│   │   ├── debug/
│   │   │   └── app-debug.apk
│   │   └── release/
│   │       └── app-release.apk
│   ├── bundle/
│   │   └── release/
│   │       └── app-release.aab
│   └── logs/
└── reports/                     # Build reports
```

---

## .gitignore Key Items

```
# Build artifacts
build/
.gradle/
*.apk
*.aab

# IDE
.idea/
*.iml
.DS_Store

# Local config
local.properties

# Secrets
secrets.properties
```

---

## Dependency Graph

```
app (main application)
├── ui layer
│   ├── (depends on) domain layer
│   └── (uses) jetpack compose
├── domain layer
│   └── (interface only)
└── data layer
    ├── (implements) domain interfaces
    └── (uses) room database
```

---

## Best Practices

1. **Keep layers separated** - UI ↔ Domain ↔ Data
2. **Mappers for conversion** - Entity ↔ Domain
3. **Use repositories** - Abstract data access
4. **Testable structure** - Easy to mock and test
5. **Single responsibility** - Each class has one job

---

## Related Docs

- [Architecture](Architecture)
- [Development Setup](Development-Setup)
- [Contributing](Contributing)

---
