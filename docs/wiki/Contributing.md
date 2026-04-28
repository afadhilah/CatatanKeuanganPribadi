# Contributing Guide

Terima kasih atas minat untuk berkontribusi ke CatatanKeuanganPribadi! Panduan ini akan membantu Anda.

---

## Jenis Kontribusi

Anda bisa berkontribusi dengan:

- **Report Bugs** - Laporkan bug yang Anda temukan
- **Suggest Features** - Usulkan fitur baru
- **Improve Documentation** - Improve wiki/README
- **Code** - Submit code untuk bug fix atau feature
- **Testing** - Test aplikasi dan report issues
- **Localization** - Terjemahan ke bahasa lain

---

## Reporting Bugs

### Sebelum Report:
1. **Check existing issues** - Mungkin sudah ada yang lapor
2. **Update ke version terbaru** - Bug mungkin sudah di-fix
3. **Reproduce consistently** - Pastikan bug consistently terjadi

### Format Report:
```markdown
### Description
Deskripsi singkat bug

### Expected Behavior
Apa yang seharusnya terjadi?

### Actual Behavior
Apa yang benar-benar terjadi?

### Steps to Reproduce
1. Step 1
2. Step 2
3. Step 3

### Environment
- Device: [contoh: Pixel 8]
- Android Version: [contoh: Android 16]
- App Version: [contoh: 1.0.0]
- Installation: [APK / Build from source]

### Screenshots/Logs
[Attach screenshot atau log jika ada]
```

### Submit Bug Report:
1. Buka [GitHub Issues](../../issues)
2. Click "New Issue" → "Bug Report"
3. Fill template
4. Submit

---

## ✨ Suggesting Features

### Sebelum Suggest:
1. Check existing features di [Features](Features.md)
2. Check [roadmap](#roadmap) untuk planned features
3. Discuss di [Discussions](../../discussions)

### Format Suggestion:
```markdown
### Feature Description
Deskripsi feature yang ingin di-add

### Use Case
Bagaimana feature ini akan digunakan?

### Why This Feature?
Mengapa feature ini penting?

### Proposed Solution (Optional)
Gimana cara mengimplementasikan? (optional)

### Alternative Solutions (Optional)
Ada solusi alternative? (optional)
```

### Submit Suggestion:
1. Buka [GitHub Discussions](../../discussions) atau [Issues](../../issues)
2. Click "New Discussion" → "Feature Requests"
3. Fill template
4. Let community discuss

---

## 💻 Code Contribution

### Setup Development Environment

```bash
# 1. Fork repository
# GitHub → Fork button

# 2. Clone your fork
git clone https://github.com/YOUR_USERNAME/CatatanKeuanganPribadi.git
cd CatatanKeuanganPribadi

# 3. Add upstream remote
git remote add upstream https://github.com/afadh/CatatanKeuanganPribadi.git

# 4. Setup development environment
# Follow: Development Setup Guide
```

### Development Workflow

#### 1. Create Feature Branch
```bash
# Update main
git fetch upstream
git checkout main
git merge upstream/main

# Create feature branch
git checkout -b feature/your-feature-name
# atau
git checkout -b fix/your-bug-fix-name
```

#### 2. Make Changes
```bash
# Edit files, implement feature/fix bug
# Write tests untuk changes Anda
# Test locally: ./gradlew build
```

#### 3. Commit Changes
```bash
# Follow conventional commits format
git add .
git commit -m "type: description"

# Examples:
# git commit -m "feat: add transaction filter"
# git commit -m "fix: fix budget calculation"
# git commit -m "docs: update readme"
# git commit -m "test: add unit tests"
```

**Commit Types:**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `test:` - Test changes
- `refactor:` - Code refactor
- `perf:` - Performance improvement
- `chore:` - Maintenance task

#### 4. Push & Create PR
```bash
# Push ke fork
git push origin feature/your-feature-name

# Buka GitHub dan create Pull Request
# Fill PR template
# Link related issues
```

### Pull Request Template

```markdown
## Description
Deskripsi singkat changes Anda

## Related Issues
Fixes #123
Related to #456

## Type of Change
- [ ] Bug fix (non-breaking)
- [ ] New feature (non-breaking)
- [ ] Breaking change
- [ ] Documentation update

## How Has This Been Tested?
Jelaskan bagaimana Anda test changes

- [ ] Unit tests added
- [ ] Manual testing done
- [ ] No test needed

## Testing Instructions
Bagaimana reviewer bisa test changes?

1. Step 1
2. Step 2

## Screenshots/Videos (if applicable)
[Attach visual evidence]

## Checklist
- [ ] Code follows style guidelines
- [ ] Comment added untuk complex logic
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests pass locally
- [ ] No breaking changes
```

---

## 📝 Code Style

### Kotlin Style Guide

#### Naming:
```kotlin
// Classes - PascalCase
class TransactionViewModel

// Functions - camelCase
fun calculateTotal()

// Constants - UPPER_SNAKE_CASE
const val MAX_TRANSACTIONS = 100

// Variables - camelCase
val totalAmount: Double
var selectedCategory: String
```

#### Formatting:
```kotlin
// Indent: 4 spaces
if (condition) {
    doSomething()
}

// Line length: max 120 characters
val veryLongVariableName = calculateSomethingComplicated(
    parameter1,
    parameter2
)

// Comments
// Use meaningful comments
// Avoid obvious comments
```

#### Structure:
```kotlin
class Transaction(
    val id: Int,
    val amount: Double
) {
    // Properties at top
    
    // Init block
    init { }
    
    // Methods
    fun calculate() { }
    
    // Companion object at bottom
    companion object { }
}
```

### Best Practices:

1. **Use Meaningful Names**
   ```kotlin
   ✓ val isValidTransaction = true
   ✗ val isValid = true
   ```

2. **Prefer Extension Functions**
   ```kotlin
   ✓ fun Double.formatCurrency() = "Rp $this"
   ✗ fun formatCurrency(amount: Double) = "Rp $amount"
   ```

3. **Use Data Classes**
   ```kotlin
   ✓ data class Transaction(val id: Int, val amount: Double)
   ```

4. **Use Sealed Classes untuk Type Safety**
   ```kotlin
   sealed class Result<T> {
       data class Success<T>(val data: T) : Result<T>()
       data class Error<T>(val exception: Exception) : Result<T>()
   }
   ```

---

## 🧪 Testing

### Unit Tests

```kotlin
// Location: app/src/test/java/

import org.junit.Test
import org.junit.Assert.*

class TransactionViewModelTest {
    @Test
    fun testCalculateTotal() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 10000.0),
            Transaction(2, 20000.0)
        )
        
        // Act
        val total = transactions.sumOf { it.amount }
        
        // Assert
        assertEquals(30000.0, total, 0.01)
    }
}
```

### Instrumented Tests

```kotlin
// Location: app/src/androidTest/java/

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click

class TransactionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testClickAddButton() {
        // Arrange
        composeTestRule.setContent {
            TransactionScreen()
        }
        
        // Act
        onView(withId(R.id.add_button)).perform(click())
        
        // Assert
        onView(withId(R.id.form_container))
            .check(matches(isDisplayed()))
    }
}
```

### Run Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Coverage
./gradlew testDebugUnitTestCoverage
```

---

## 📚 Documentation

### Update Documentation

1. **README.md** - For overview
2. **Wiki files** - Detailed documentation
3. **Code comments** - For complex logic
4. **Changelog** - For release notes

### Documentation Format

```markdown
# Heading 1

## Heading 2

### Heading 3

**Bold** untuk important points
- Bullet points
- For lists
  - Nested items

> Quote blocks for important info

\`\`\`kotlin
// Code blocks
fun example() { }
\`\`\`

[Links](https://example.com)
```

---

## 🔄 Roadmap

### v1.0 ✅ (Current)
- ✅ Core features (transaction, account, budget)
- ✅ Dashboard & statistics
- ✅ Local database

### v1.1 🔄 (In Progress)
- 🔄 Cloud sync
- 🔄 Advanced export

### v2.0 📋 (Planned)
- 📋 AI-powered expense prediction
- 📋 Multi-user support
- 📋 Web dashboard
- 📋 Mobile app for other platforms

---

## 🎓 Resources

- [Android Developer Guide](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## 📞 Need Help?

- 💬 [GitHub Discussions](../../discussions)
- 🐛 [Issues](../../issues)
- 📧 Email: support@catatankeuangan.com

---

## 🙏 Thank You!

Terima kasih telah berkontribusi untuk membuat CatatanKeuanganPribadi lebih baik!

---

**Last Updated**: April 29, 2026
