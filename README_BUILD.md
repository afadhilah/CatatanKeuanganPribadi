# CatatanKeuanganPribadi - Build & Compile Guide

Panduan troubleshooting dan setup untuk masalah build Gradle, Java version, dan compile errors.

## ⚡ Quick Diagnosis

Jika mengalami error saat build, ikuti checklist ini:

```
1. ✅ JDK version is 21 (not 25)
   → java -version
   → Check output: "21.0.x"

2. ✅ gradle.properties has org.gradle.java.home set
   → Already configured to use JDK 21

3. ✅ Gradle daemon is restarted
   → .\gradlew.bat --stop
   → .\gradlew.bat :app:assembleDebug

4. ✅ Build cache cleared
   → .\gradlew.bat clean :app:assembleDebug
```

If all pass → Build should succeed.

---

## 🔧 Common Build Errors & Fixes

### ❌ Error: `java.lang.IllegalArgumentException: 25.0.1`

**Cause:** Gradle is running on Java 25 (incompatible with Kotlin 2.0.21)

**Fix:**

1. Ensure JDK 21 is installed:
```powershell
Test-Path "C:\Program Files\Java\jdk-21"
# Output: True
```

2. Verify gradle.properties has JDK 21 path:
```powershell
Get-Content gradle.properties | findstr "org.gradle.java.home"
# Output: org.gradle.java.home=C:\\Program Files\\Java\\jdk-21
```

3. Kill Gradle daemon and retry:
```powershell
.\gradlew.bat --stop
.\gradlew.bat :app:compileDebugKotlin
```

### ❌ Error: `Unresolved reference 'ExposedDropdownMenu'`

**Cause:** Using deprecated Compose APIs

**Status:** Already fixed in codebase (using stable DropdownMenu)

If you get this error:
1. Update to latest Compose BOM:
```powershell
# In gradle/libs.versions.toml
composeBom = "2024.09.00"  # or later
```

2. Rebuild:
```powershell
.\gradlew.bat clean :app:compileDebugKotlin
```

### ❌ Error: `Cannot infer type for this parameter`

**Cause:** Suspend function method reference in `.let()` or lambda

**Status:** Already fixed in [data/repository/](app/src/main/java/com/example/catatankeuanganpribadi/data/repository/)

If this appears elsewhere:
```kotlin
// ❌ Wrong (will fail)
budgetDao.getBudgetById(budgetId)?.let(budgetDao::deleteBudget)

// ✅ Correct
val budget = budgetDao.getBudgetById(budgetId)
if (budget != null) {
    budgetDao.deleteBudget(budget)
}
```

### ❌ Error: `This declaration only available in Kotlin 2.1`

**Cause:** Using Kotlin 2.1+ APIs but project targets Kotlin 2.0.21

**Status:** Already fixed by downgrading kotlinx-datetime to 0.6.1

If you update dependencies:
```toml
# gradle/libs.versions.toml
kotlin = "2.0.21"              # Do NOT upgrade to 2.1+
kotlinxDatetime = "0.6.1"      # NOT 0.7.0+
```

### ❌ Error: `Execution failed for task ':app:compileDebugKotlin'`

**Steps to debug:**

```powershell
# 1. Get full stacktrace
.\gradlew.bat :app:compileDebugKotlin --stacktrace

# 2. Get more verbose output
.\gradlew.bat :app:compileDebugKotlin -i

# 3. Clear build cache & retry
.\gradlew.bat clean :app:compileDebugKotlin
```

Then search error message in:
- [Kotlin docs](https://kotlinlang.org/docs)
- [Android Gradle Plugin docs](https://developer.android.com/build)
- Project issues

---

## 🔍 Gradle Debugging

### Check which JDK Gradle is using

```powershell
.\gradlew.bat -version
```

Output should show:
```
Launcher JVM:    C:\Program Files\Java\jdk-21 (JDK 21)
Daemon JVM:      C:\Program Files\Java\jdk-21 (from org.gradle.java.home)
```

If NOT JDK 21, edit [gradle.properties](gradle.properties):
```properties
org.gradle.java.home=C:\\Program Files\\Java\\jdk-21
```

### View Gradle tasks

```powershell
.\gradlew.bat tasks
```

Useful tasks:
- `assembleDebug` - Build debug APK
- `compileDebugKotlin` - Compile Kotlin only
- `clean` - Delete build folder
- `build` - Full build (compile + unit tests)

### Force Gradle to recompile everything

```powershell
.\gradlew.bat clean :app:assembleDebug --no-build-cache
```

---

## 📦 Dependency Management

### Check for dependency conflicts

```powershell
.\gradlew.bat :app:dependencies
```

### Update all dependencies safely

Edit [gradle/libs.versions.toml](gradle/libs.versions.toml):
```toml
[versions]
# Update carefully, test after each change
kotlin = "2.0.21"              # Pinned to this version
agp = "8.11.2"                 # AGP 9.0+ may have breaking changes
composeBom = "2024.09.00"      # Update to latest stable
```

After update:
```powershell
.\gradlew.bat clean :app:build
```

---

## 🛠️ Setup Issues

### Java version not found

```powershell
# Check installed JDKs
Get-ChildItem "C:\Program Files\Java"

# Install JDK 21 from:
# https://www.oracle.com/java/technologies/downloads/
```

### Android SDK not found

```powershell
# Check SDK path
$env:ANDROID_HOME

# If empty, set it
[Environment]::SetEnvironmentVariable("ANDROID_HOME", "C:\Users\<user>\AppData\Local\Android\Sdk", "User")

# Then restart terminal
```

### Gradle wrapper permission denied (Linux/Mac)

```bash
chmod +x gradlew
./gradlew :app:assembleDebug
```

---

## 📊 Build Performance

### Speed up builds

1. **Increase Gradle heap**
   
   Edit [gradle.properties](gradle.properties):
   ```properties
   org.gradle.jvmargs=-Xmx4096m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
   ```

2. **Parallel compilation**
   
   Already enabled:
   ```properties
   org.gradle.parallel=true
   org.gradle.workers.max=8
   ```

3. **Use offline mode** (if no dependency updates needed)
   ```powershell
   .\gradlew.bat --offline :app:assembleDebug
   ```

4. **Disable unused task**
   ```powershell
   .\gradlew.bat :app:assembleDebug -x :app:testDebugUnitTest
   ```

---

## 🔐 Signing & Release Build

### Debug build (default, no signing needed)
```powershell
.\gradlew.bat :app:assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release build (requires keystore)
```powershell
.\gradlew.bat :app:assembleRelease
# Will ask for keystore/key password OR use env vars
```

Set env vars:
```powershell
$env:KEYSTORE_PASSWORD="your_pass"
$env:KEY_PASSWORD="your_key_pass"
.\gradlew.bat :app:assembleRelease
```

---

## 📝 Build Output

After successful build:

```
BUILD SUCCESSFUL in 54s
35 actionable tasks: 8 executed, 27 up-to-date
```

APK location:
```
app/build/outputs/apk/debug/app-debug.apk        (Debug)
app/build/outputs/apk/release/app-release.apk    (Release, if built)
```

---

## ✅ Verification Checklist

After setup, verify:

```powershell
# 1. JDK version
java -version
# Expected: 21.0.x or higher (NOT 25)

# 2. Gradle version
.\gradlew.bat --version
# Expected: Gradle 8.13, Launcher JVM: JDK 21

# 3. Android SDK
adb --version
# Should print version and work

# 4. Build successfully
.\gradlew.bat clean :app:assembleDebug
# Expected: BUILD SUCCESSFUL
```

If all pass: **You're ready to build & run!**

---

## 🆘 Still Stuck?

1. Check error message carefully (copy full stacktrace)
2. Search on [Stack Overflow](https://stackoverflow.com/questions/tagged/android-gradle)
3. Check [Gradle documentation](https://docs.gradle.org)
4. Post issue with:
   - Java version (`java -version`)
   - Gradle version (`.\gradlew.bat --version`)
   - Full error message & stacktrace
   - OS (Windows/Mac/Linux)

---

## 📚 Related Documentation

- [README.md](README.md) - Main README
- [README_DEVICE.md](README_DEVICE.md) - Run on physical device
- [README_EMULATOR.md](README_EMULATOR.md) - Run on emulator
- [README_TESTS.md](README_TESTS.md) - Running tests
