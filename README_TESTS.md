# CatatanKeuanganPribadi - Testing Guide

Panduan lengkap untuk menjalankan unit tests dan instrumentation tests.

## 🧪 Test Structure

```
app/src/
├── test/
│   └── java/com/example/catatankeuanganpribadi/
│       └── [Unit tests - run on local JVM]
│
└── androidTest/
    └── java/com/example/catatankeuanganpribadi/
        └── [Instrumentation tests - run on device/emulator]
```

---

## 🏃 Unit Tests

### Run all unit tests

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

**Output:**
```
BUILD SUCCESSFUL in 45s
:app:testDebugUnitTest PASSED
```

### Run specific test class

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "*TransactionRepositoryTest*"
```

### Run with coverage report

```powershell
.\gradlew.bat :app:testDebugUnitTestCoverage
```

Coverage report location:
```
app/build/reports/coverage/testDebugUnitTest/index.html
```

---

## 📱 Instrumentation Tests

### Prerequisites

- Android Emulator OR physical device connected
- Emulator running or device online

```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb devices
# Output: emulator-5554  device  (or device serial)
```

### Run all instrumentation tests

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

**Output:**
```
BUILD SUCCESSFUL in 2m 15s
:app:connectedDebugAndroidTest PASSED
Tests run: 5, Passed: 5, Failed: 0
```

### Run specific test class

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest --tests "*TransactionUITest*"
```

### Run with custom instrumentation

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small
```

Options: `small`, `medium`, `large`

---

## 🔍 Test Locations

### Unit Test Examples

**ViewModel Tests:**
```
app/src/test/java/com/example/catatankeuanganpribadi/presentation/
  ├── AddTransactionViewModelTest.kt
  ├── TransactionListViewModelTest.kt
  └── BudgetViewModelTest.kt
```

**Repository Tests:**
```
app/src/test/java/com/example/catatankeuanganpribadi/data/repository/
  ├── TransactionRepositoryTest.kt
  ├── AccountRepositoryTest.kt
  └── BudgetRepositoryTest.kt
```

**Use Case Tests:**
```
app/src/test/java/com/example/catatankeuanganpribadi/domain/usecase/
  ├── SaveTransactionUseCaseTest.kt
  └── DeleteTransactionUseCaseTest.kt
```

### Instrumentation Test Examples

**UI Tests:**
```
app/src/androidTest/java/com/example/catatankeuanganpribadi/presentation/
  ├── DashboardScreenTest.kt
  ├── AddTransactionScreenTest.kt
  └── TransactionListScreenTest.kt
```

---

## 📝 Writing Tests

### Unit Test Template

```kotlin
class AddTransactionViewModelTest {
    
    private lateinit var viewModel: AddTransactionViewModel
    private lateinit var mockAccountRepository: AccountRepository
    
    @Before
    fun setUp() {
        // Setup mocks
        mockAccountRepository = mockk()
        viewModel = AddTransactionViewModel(mockAccountRepository)
    }
    
    @Test
    fun testUpdateAmount() {
        viewModel.updateAmount("1000")
        assertEquals("1000", viewModel.uiState.value.amount)
    }
}
```

### Instrumentation Test Template

```kotlin
@RunWith(AndroidJUnit4::class)
class AddTransactionScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testAddTransactionFlow() {
        composeTestRule.setContent {
            AddTransactionScreen(
                viewModel = /* test viewModel */
            )
        }
        
        composeTestRule
            .onNodeWithTag("amountInput")
            .performTextInput("50000")
        
        composeTestRule
            .onNodeWithTag("saveButton")
            .performClick()
    }
}
```

---

## 🛠️ Test Setup Requirements

### Mocking Framework

```toml
# gradle/libs.versions.toml
mockk = "1.13.8"               # Mock library
junit = "4.13.2"               # Test framework
androidxTestCore = "1.5.0"     # Android test utilities
```

### Common Test Dependencies

```kotlin
// Unit Test
testImplementation "junit:junit:${libs.versions.junit}"
testImplementation "io.mockk:mockk:${libs.versions.mockk}"

// Instrumentation Test
androidTestImplementation "androidx.test.ext:junit:${libs.versions.androidxTestExt}"
androidTestImplementation "androidx.compose.ui:ui-test-junit4:${libs.versions.composeBom}"
```

---

## 📊 Test Reports

### View Test Results

After running tests, view HTML report:

**Unit Tests:**
```
app/build/reports/tests/testDebugUnitTest/index.html
```

**Instrumentation Tests:**
```
app/build/reports/androidTests/connected/index.html
```

### CI/CD Integration

To parse results in CI:

```powershell
# JUnit XML format (for GitHub Actions, GitLab CI, etc.)
$results = "app/build/test-results/testDebugUnitTest/TEST-*.xml"
```

---

## 🚀 Performance Testing

### Benchmark instrumentation test

```kotlin
@RunWith(AndroidJUnit4::class)
class DashboardPerformanceTest {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun testDashboardRenderTime() {
        benchmarkRule.measureRepeated {
            // Measure rendering time
            composeTestRule.setContent { DashboardScreen() }
        }
    }
}
```

Run with:
```powershell
.\gradlew.bat :app:connectedAndroidTest --tests "*Performance*"
```

---

## 📋 Test Checklist

Before committing code:

```
Unit Tests
✅ Run all unit tests pass
✅ Coverage > 70% for critical paths
✅ Mock external dependencies
✅ Test edge cases (null, empty, max values)

Instrumentation Tests
✅ Run on emulator & physical device
✅ Test user interactions (click, input, scroll)
✅ Test navigation between screens
✅ Test error handling & UI feedback

Integration Tests
✅ End-to-end flows work
✅ Database operations work
✅ Network calls mocked
✅ State management correct
```

---

## 🔧 Debugging Tests

### Debug unit test

```powershell
# Add verbose output
.\gradlew.bat :app:testDebugUnitTest --info

# Run with stacktrace
.\gradlew.bat :app:testDebugUnitTest --stacktrace
```

### Debug instrumentation test

```powershell
# View Logcat while tests run
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb logcat -s "CatatanKeuanganPribadi"

# In another terminal, run tests
.\gradlew.bat :app:connectedAndroidTest
```

### Common Test Failures

| Error | Cause | Fix |
|-------|-------|-----|
| `Test timed out` | Coroutine not completing | Use `runBlockingTest {}` |
| `Activity not found` | Screen not composed | Set content in `composeTestRule` |
| `Assertion failed` | Expected != Actual | Review mock data setup |
| `Device offline` | Emulator/device disconnected | Restart emulator/device |

---

## 💡 Best Practices

1. **Write tests alongside code** - Not after
2. **Test behaviors, not implementations** - Focus on outputs
3. **Mock external dependencies** - Database, network, OS
4. **Use test fixtures** - Reusable test data builders
5. **Arrange-Act-Assert** - Clear test structure
6. **Test edge cases** - Null, empty, boundary values
7. **Keep tests fast** - Optimize for quick feedback
8. **Test critical paths** - Focus on revenue features

---

## 📚 Related Documentation

- [README.md](README.md) - Main README
- [README_BUILD.md](README_BUILD.md) - Build & compile guide
- [README_DEVICE.md](README_DEVICE.md) - Run on physical device
- [README_EMULATOR.md](README_EMULATOR.md) - Run on emulator

---

## 🔗 Useful Links

- [JUnit 4 Documentation](https://junit.org/junit4/)
- [MockK Documentation](https://mockk.io/)
- [Compose Testing Guide](https://developer.android.com/jetpack/compose/testing)
- [Android Instrumented Tests](https://developer.android.com/training/testing/instrumented-tests)
