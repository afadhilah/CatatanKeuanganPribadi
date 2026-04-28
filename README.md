# CatatanKeuanganPribadi - Personal Finance Tracker

Aplikasi Android untuk mencatat dan mengelola keuangan pribadi dengan fitur transaksi, budget, dan statistik.

## Features

**Transaksi** - Catat income, expense, dan transfer  
**Dashboard** - Ringkasan saldo, income, dan expense  
**Daftar Transaksi** - Filter by period, account, category, search  
**Budget** - Set budget per kategori dan monitor usage  
**Statistik** - Visualisasi distribusi pengeluaran per kategori  
**Multiple Accounts** - Manage cash, bank, e-wallet accounts  

## Tech Stack

- **Language**: Kotlin 2.0.21
- **Architecture**: Clean Architecture + MVVM
- **UI**: Jetpack Compose
- **Database**: Room
- **Build**: Gradle 8.13, AGP 8.11.2
- **Target**: Android 12+ (API 31+), Built for Android 16 (API 36)

---

## Quick Start

### Prerequisites
- JDK 21 (not Java 25!)
- Android SDK 36.0.0 or higher
- Android Emulator or Physical Device

### Run on Physical Device

```powershell
.\run-device.ps1
```

See detailed guide: [README_DEVICE.md](README_DEVICE.md)

### Run on Emulator

```powershell
.\run-android.ps1
```

See detailed guide: [README_EMULATOR.md](README_EMULATOR.md)

### Build Only

```powershell
.\gradlew.bat :app:assembleDebug
```

See build troubleshooting: [README_BUILD.md](README_BUILD.md)

---

## Project Structure

```
CatatanKeuanganPribadi/
├── app/
│   ├── src/main/java/com/example/catatankeuanganpribadi/
│   │   ├── domain/          # Business logic, use cases, repositories
│   │   ├── data/            # Repository implementations, DB, mappers
│   │   ├── presentation/    # ViewModels, UI screens, components
│   │   ├── di/              # Dependency injection
│   │   └── MainActivity.kt
│   ├── src/test/            # Unit tests
│   ├── src/androidTest/     # Instrumentation tests
│   ├── build.gradle.kts
│   └── schemas/             # Room database schema
├── gradle/
│   └── libs.versions.toml   # Dependency versions
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties        # Gradle config (JDK 21 pinned)
├── run-device.ps1          # One-command: build+install+launch on device
├── run-android.ps1         # One-command: build+install+launch on emulator
└── README_*.md             # Specific documentation
```

---

## Running the App

### Quickest Way

**Physical Device:**
```powershell
.\run-device.ps1
```

**Emulator:**
```powershell
.\run-android.ps1
```

Both scripts:
1. Build debug APK
2. Install on device/emulator
3. Launch app automatically

---

## Testing

### Unit Tests
```powershell
.\gradlew.bat :app:testDebugUnitTest
```

### Instrumentation Tests (on device/emulator)
```powershell
.\gradlew.bat :app:connectedAndroidTest
```

---

## Documentation

- **[README_DEVICE.md](README_DEVICE.md)** - Run on physical Android device  
- **[README_EMULATOR.md](README_EMULATOR.md)** - Run on Android Emulator  
- **[README_BUILD.md](README_BUILD.md)** - Build troubleshooting & JDK setup  
- **[README_TESTS.md](README_TESTS.md)** - Unit & instrumentation tests  

---

## Configuration

### JDK Version
Default JDK: **21** (configured in [gradle.properties](gradle.properties))

If you have Java 25 installed (incompatible), the build automatically uses JDK 21.

### SDK Requirements
- compileSdk: 36
- minSdk: 24
- targetSdk: 36

---

## Dependencies

Key libraries:
- `androidx.lifecycle:lifecycle-runtime-compose` - Lifecycle & Compose integration
- `androidx.room:room-runtime` - Local database
- `androidx.navigation:navigation-compose` - Navigation
- `org.jetbrains.kotlinx:kotlinx-coroutines-android` - Async programming
- `org.jetbrains.kotlinx:kotlinx-datetime` - Date/time utilities

Full list: [gradle/libs.versions.toml](gradle/libs.versions.toml)

---

## Known Issues

- Emulator slow on low-end PC? → Allocate more RAM in AVD config (4GB+)
- Compile error with Java 25? → JDK 21 is automatically selected via gradle.properties
- ADB device offline? → Run `adb kill-server && adb devices`

---

## License

This is a personal finance tracker app created for educational purposes.

---

## FAQ

**Q: Can I run without emulator?**  
A: Yes! Connect Android device via USB, enable USB Debugging, and run `.\run-device.ps1`

**Q: Why Java 21?**  
A: Kotlin 2.0.21 + Gradle 8.13 + latest AGP require JDK 21. Java 25 has breaking changes not yet supported.

**Q: How to add new transactions?**  
A: Tap the **+** FAB on dashboard/any screen → fill transaction details → save

**Q: Can I export data?**  
A: Currently, data is stored locally in SQLite. Export feature coming soon.

---

## Useful Links

- [Android Developer Docs](https://developer.android.com)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
