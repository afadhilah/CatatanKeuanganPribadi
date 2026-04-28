# Development Setup

Panduan lengkap untuk setup environment development CatatanKeuanganPribadi.

---

## Prerequisites

Sebelum memulai, pastikan Anda sudah install:

### 1. **JDK 21** PENTING!
**JANGAN gunakan Java 25!**

#### Windows:
```powershell
# Install via Chocolatey
choco install openjdk21

# Atau download dari: https://adoptium.net/
```

#### macOS:
```bash
# Install via Homebrew
brew install openjdk@21

# Setup environment variable
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

#### Linux (Ubuntu):
```bash
sudo apt-get install openjdk-21-jdk
```

**Verify:**
```bash
java -version
# Output: openjdk version "21.0.x"
```

---

### 2. **Android Studio** (Recommended)
- Download: https://developer.android.com/studio
- Minimal version: Electric Eel atau lebih baru

---

### 3. **Android SDK**
Bisa auto-download via Android Studio atau manual:

```bash
# Via sdkmanager
sdkmanager "platforms;android-36"
sdkmanager "platforms;android-34"
sdkmanager "build-tools;36.0.0"
```

**Min SDK**: API 24 (Android 7.0)  
**Target SDK**: API 36 (Android 16)  
**Compile SDK**: 36

---

### 4. **Gradle**
Version 8.13+ (included in project)

---

## Clone Repository

```bash
# Clone dari GitHub
git clone https://github.com/afadh/CatatanKeuanganPribadi.git

# Navigate ke folder
cd CatatanKeuanganPribadi
```

---

## Setup Environment

### 1. **Create local.properties**

File ini adalah wajib untuk Android SDK path.

```properties
# local.properties
sdk.dir=/path/to/Android/sdk

# Example:
# Windows
sdk.dir=C:\\Users\\YourUsername\\AppData\\Local\\Android\\sdk

# macOS
sdk.dir=/Users/YourUsername/Library/Android/sdk

# Linux
sdk.dir=/home/username/Android/sdk
```

---

### 2. **Gradle Configuration**

File `gradle.properties` sudah ada, tapi bisa customize:

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
```

---

## Build & Run

### 1. **Build Project**

```bash
# Clean build
./gradlew clean build

# Build only
./gradlew build

# Build with verbose output
./gradlew build --info
```

### 2. **Build APK (Debug)**

```bash
# Debug APK untuk testing
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

### 3. **Build APK (Release)**

```bash
# Release APK untuk production
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

### 4. **Install ke Device/Emulator**

```bash
# Install debug APK
./gradlew installDebug

# Run di emulator/device
./gradlew installDebugAndRunTests
```

---

## Run on Emulator

### Via Terminal:

```bash
# Run dengan emulator
./gradlew emulator

# Atau
adb shell app_process /system/bin com.android.commands.am.Am start com.example.catatankeuanganpribadi/.MainActivity
```

### Via PowerShell (Windows):

```powershell
# Run emulator script
.\run-android.ps1
```

### Via Android Studio:
1. Buka project di Android Studio
2. Klik **Run** → **Run 'app'**
3. Pilih emulator/device
4. Klik OK

---

## Run on Physical Device

### Prerequisites:
- Device sudah connect via USB
- USB Debugging aktif di device
- ADB driver installed (for Windows)

### Run:

```powershell
# PowerShell (Windows)
.\run-device.ps1

# Atau manual
./gradlew installDebug
adb shell am start -n com.example.catatankeuanganpribadi/.MainActivity
```

---

## IDE Setup (Android Studio)

### 1. **Import Project**
```
File → Open → Pilih folder CatatanKeuanganPribadi
```

### 2. **Sync Gradle**
```
File → Sync Now
Atau: Ctrl+Shift+I (Windows/Linux) atau Cmd+Shift+I (Mac)
```

### 3. **Setup JDK**
```
File → Project Structure → SDK Location
Pastikan JDK 21 dipilih
```

### 4. **Configure Emulator** (Optional)
```
Tools → AVD Manager → Create New Device
Pilih Pixel 8 atau device lain
Select API 34+ (untuk compatibility)
```

---

## Run Tests

### Unit Tests:

```bash
# Run semua unit tests
./gradlew test

# Run specific test
./gradlew test --tests "com.example.catatankeuanganpribadi.domain.usecase.*"

# Run dengan report
./gradlew test --no-offline --no-build-cache
```

### Instrumented Tests (UI Tests):

```bash
# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific instrumented test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.catatankeuanganpribadi.ui.TransactionScreenTest
```

### Code Coverage:

```bash
# Generate coverage report
./gradlew testDebugUnitTestCoverage

# Output: app/build/reports/coverage/
```

---

## Debug

### Debug di Android Studio:

1. Set breakpoint (klik di line number)
2. Run dengan **Debug** mode:
   ```
   Run → Debug 'app'
   ```
3. Android Studio akan pause di breakpoint
4. Inspect variables di Debug panel

### Debug via Logcat:

```bash
# View logs
adb logcat

# Filter logs
adb logcat | grep CatatanKeuangan

# Tag filter
adb logcat -s TAG
```

---

## Project Structure

```
CatatanKeuanganPribadi/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/catatankeuanganpribadi/
│   │   │   │   ├── ui/          (Presentation Layer)
│   │   │   │   ├── domain/      (Domain Layer)
│   │   │   │   ├── data/        (Data Layer)
│   │   │   │   └── di/          (Dependency Injection)
│   │   │   └── res/
│   │   ├── test/                (Unit Tests)
│   │   └── androidTest/         (Instrumented Tests)
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## Useful Commands

```bash
# Clean everything
./gradlew clean

# Build & run tests
./gradlew build

# Format code (if configured)
./gradlew ktlintFormat

# Check dependencies
./gradlew dependencies

# Generate APK untuk release
./gradlew bundleRelease

# Upgrade gradle wrapper
./gradlew wrapper --gradle-version 8.13
```

---

## Troubleshooting

### Error: "This tool requires JDK 17 or later"
**Solution:** Install JDK 21, set `JAVA_HOME`

### Error: "SDK location not found"
**Solution:** Create `local.properties` dengan Android SDK path

### Error: "Gradle sync failed"
**Solution:** 
```bash
./gradlew clean
./gradlew --stop
./gradlew sync
```

### Device not recognized
**Solution:**
```bash
adb kill-server
adb start-server
adb devices
```

---

## Resources

- [Android Developer Guide](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Android Studio Documentation](https://developer.android.com/studio/intro)

---