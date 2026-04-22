# Running CatatanKeuanganPribadi on Android Emulator

Panduan lengkap untuk menjalankan aplikasi di Android Virtual Device (emulator).

## Prasyarat

- Android SDK dengan emulator system image (Android 12+)
- Virtualization enabled di BIOS (Hyper-V, KVM, atau WHPX)
- Minimal 4GB RAM tersedia untuk emulator
- Gradle dan Java JDK 21 sudah tersetup (lihat [README_BUILD.md](README_BUILD.md))

---

## Langkah 1: Setup AVD (Android Virtual Device)

### Check AVD yang Tersedia

```powershell
$emulator = "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe"
& $emulator -list-avds
```

Jika ada AVD dengan API 36 (Android 16), langsung ke Langkah 2. Jika belum ada:

### Buat AVD Baru (Optional)

1. Buka **Android Studio** → **Device Manager**
2. Click **Create Device** → pilih **Phone**
3. Pilih device profile (e.g., "Medium Phone")
4. Click **Next** → pilih **API 36** (Baklava) → **Next**
5. Set nama: `Medium_Phone_API_36.0` → **Finish**

Atau via command line:
```powershell
$avdmanager = "$env:LOCALAPPDATA\Android\Sdk\cmdline-tools\latest\bin\avdmanager.bat"
& $avdmanager create avd -n "Medium_Phone_API_36.0" -k "system-images;android-36;google_apis_playstore;x86_64" -d "medium"
```

---

## Langkah 2: Start Emulator & Build

### Option A: Gunakan Script Shortcut (Recommended)

```powershell
.\run-android.ps1
```

**Output:**
```
========================================
  CatatanKeuanganPribadi - Emulator Build
========================================

[1/4] Starting emulator...
  Waiting for emulator to boot...
✓ Emulator ready

[2/4] Building debug APK...
✓ Build successful

[3/4] Installing APK on emulator...
✓ Installation successful

[4/4] Launching app...
✓ App launched on emulator!
========================================
```

### Option B: Manual Steps

```powershell
# 1. Start emulator
$emulator = "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe"
& $emulator -avd "Medium_Phone_API_36.0"

# 2. Di terminal baru, build & install
.\gradlew.bat :app:installDebug

# 3. Launch app
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb shell monkey -p com.example.catatankeuanganpribadi -c android.intent.category.LAUNCHER 1
```

---

## Langkah 3: Monitor App (Optional)

### View Live Logs

```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb logcat | findstr CatatanKeuanganPribadi
```

### Stop/Clear App

```powershell
# Stop running app
& $adb shell am force-stop com.example.catatankeuanganpribadi

# Clear app data
& $adb shell pm clear com.example.catatankeuanganpribadi

# Uninstall app
& $adb uninstall com.example.catatankeuanganpribadi
```

---

## Troubleshooting

### ❌ Emulator tidak start atau super lambat
- Pastikan **Virtualization enabled** di BIOS (Intel VT-x atau AMD-V)
- Check Graphics mode di AVD config (HAM/Vulkan > Software)
- Close heavy apps di PC (browser, IDE, IDE)
- Start emulator dengan no-snapshot:
  ```powershell
  & $emulator -avd "Medium_Phone_API_36.0" -no-snapshot-load
  ```

### ❌ ADB "device offline"
```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb kill-server
& $adb devices
```

### ❌ Installation failed: "INSTALL_FAILED_INVALID_APK"
- Build ulang dengan clean:
  ```powershell
  .\gradlew.bat clean :app:assembleDebug
  ```

### ❌ App crash after launch
- Check logs:
  ```powershell
  $adb logcat -v threadtime | findstr FATAL
  ```
- Restart emulator dan reinstall

### ❌ "No space left on device"
```powershell
# Clear emulator data
& $adb shell pm trim-caches 1073741824

# Or delete AVD and recreate
& $emulator -list-avds
# Delete folder: C:\Users\<user>\.android\avd\<avd_name>.avd
```

---

## Performance Optimization

1. **Allocate more RAM to emulator**
   - Edit `~/.android/avd/Medium_Phone_API_36.0/config.ini`
   - Change: `hw.ramSize=2048` → `hw.ramSize=4096`

2. **Use Hardware Acceleration**
   - Emulator automatically uses WHPX (Windows) / KVM (Linux) / HVF (Mac)

3. **Enable "Store" app for faster install**
   - AVD with "Play Store" system image has better optimization

---

## Useful ADB Commands

```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

# List running devices
& $adb devices

# Install APK
& $adb install .\app\build\outputs\apk\debug\app-debug.apk

# Uninstall app
& $adb uninstall com.example.catatankeuanganpribadi

# Launch activity
& $adb shell am start -n com.example.catatankeuanganpribadi/.MainActivity

# View all logs
& $adb logcat

# Stop app
& $adb shell am force-stop com.example.catatankeuanganpribadi

# Clear app data (keeps app installed)
& $adb shell pm clear com.example.catatankeuanganpribadi

# Screenshot
& $adb shell screencap -p /sdcard/screenshot.png
& $adb pull /sdcard/screenshot.png .\screenshot.png

# Open file manager on emulator
& $adb shell am start -a android.intent.action.VIEW
```

---

## Emulator Device Specifications

AVD: **Medium_Phone_API_36.0**
- Android Version: 16.0 (API 36, Baklava)
- Resolution: 1080x2400 (FHD+)
- DPI: 420
- RAM: 2GB (default, can be increased)
- Storage: 100GB (virtual)

---

## Next Steps

- Untuk menjalankan di device fisik, lihat [README_DEVICE.md](README_DEVICE.md)
- Jika terjadi issue saat compile, lihat [README_BUILD.md](README_BUILD.md)
- Untuk running tests, lihat [README_TESTS.md](README_TESTS.md)
