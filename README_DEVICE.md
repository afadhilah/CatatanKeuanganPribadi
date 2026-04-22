# Running CatatanKeuanganPribadi on Physical Android Device

Panduan lengkap untuk menjalankan aplikasi di device Android fisik (bukan emulator).

## Prasyarat

- Android device dengan Android 6.0 (API 24) atau lebih baru
- USB cable (USB Type-C atau Micro-USB sesuai device)
- PC dengan Android SDK Platform Tools terpasang
- Gradle dan Java JDK 21 sudah tersetup (lihat [README_BUILD.md](README_BUILD.md))

---

## Langkah 1: Aktifkan Developer Mode & USB Debugging di Device

### Untuk Android 11+:
1. Buka **Settings** → **About phone**
2. Scroll ke bawah, tap **Build number** 7 kali sampai muncul: "You are now a developer!"
3. Kembali ke Settings → cari **Developer options**
4. Tap **Developer options** → aktifkan **USB Debugging**

### Untuk Android 6-10:
1. Buka **Settings** → **About phone**
2. Tap **Build number** 7 kali sampai developer mode aktif
3. Kembali ke Settings → **Developer options**
4. Aktifkan **USB Debugging**

---

## Langkah 2: Hubungkan Device ke PC

1. **Gunakan USB cable** (original atau berkualitas) untuk connect device ke PC
2. Device akan tampil popup: **"Allow USB debugging?"** → pilih **Allow** dan check **Always allow from this computer**
3. Buka PowerShell di folder project dan jalankan:
   ```powershell
   $adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
   & $adb devices
   ```
4. Jika berhasil, akan muncul:
   ```
   List of devices attached
   XXXXXXXXXXXXXX    device
   ```
   (Jika status = "unauthorized", pastikan sudah allow di device)

---

## Langkah 3: Build & Install APK ke Device

### Option A: Gunakan Gradle Command (Recommended)

```powershell
.\gradlew.bat :app:installDebug
```

**Output sukses:**
```
Installing APK 'app-debug.apk' on 'DeviceName' for :app:debug
Installed on 1 device.

BUILD SUCCESSFUL in 1m 2s
```

### Option B: Gunakan Script Shortcut (Build + Install + Launch)

```powershell
.\run-device.ps1
```

(Lihat [run-device.ps1](run-device.ps1) di folder project)

---

## Langkah 4: Jalankan Aplikasi

### Option A: Dari Terminal (Automatic)
```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
& $adb shell monkey -p com.example.catatankeuanganpribadi -c android.intent.category.LAUNCHER 1
```

### Option B: Manual dari Device
1. Di device, buka **App Drawer** atau home screen
2. Cari app **Catatan Keuangan Pribadi**
3. Tap icon untuk membuka

---

## Troubleshooting

### ❌ Device tidak terdeteksi
- Pastikan USB Debugging sudah aktif di device
- Coba USB cable lain (kabel charging bisa saja tidak support data transfer)
- Restart device dan PC
- Run: `adb kill-server` lalu `adb devices` lagi

### ❌ Status "unauthorized"
- Di device, akan ada popup "Allow USB debugging?" → tap **Allow**
- Check **Always allow from this computer**
- Retry: `adb devices`

### ❌ "Device offline"
- Disconnect USB, tunggu 5 detik, sambungkan lagi
- Di Settings device → Developer options → disable lalu enable USB Debugging
- Restart adb: `adb kill-server && adb devices`

### ❌ Installation failed
```
com.android.install_failed_insufficient_storage
```
→ Hapus aplikasi lama atau aplikasi lain yang tidak perlu untuk space

```
INSTALL_FAILED_USER_RESTRICTED
```
→ Device mungkin memiliki child/guest account aktif, switch ke owner account

### ❌ App crash setelah install
- Check logs: `adb logcat | findstr CatatanKeuanganPribadi`
- Hapus app: `adb uninstall com.example.catatankeuanganpribadi`
- Build ulang dan install: `.\gradlew.bat :app:installDebug`

---

## Useful ADB Commands

```powershell
# List devices
adb devices

# Install APK
adb install .\app\build\outputs\apk\debug\app-debug.apk

# Uninstall app
adb uninstall com.example.catatankeuanganpribadi

# Launch app
adb shell am start -n com.example.catatankeuanganpribadi/.MainActivity

# View live logs
adb logcat

# Stop app
adb shell am force-stop com.example.catatankeuanganpribadi

# Clear app data
adb shell pm clear com.example.catatankeuanganpribadi
```

---

## Performance Tips

- **Close unnecessary apps** di device sebelum jalankan (save RAM & battery)
- **Connect ke WiFi** jika ADB over TCP lebih dipilih (advanced)
- **Keep screen on** saat development (Settings → Developer options → Stay awake)

---

## Quick Reference: One-Command Build + Install + Launch

```powershell
# Build APK
.\gradlew.bat :app:assembleDebug

# Install & launch
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"; & $adb install .\app\build\outputs\apk\debug\app-debug.apk; & $adb shell monkey -p com.example.catatankeuanganpribadi -c android.intent.category.LAUNCHER 1
```

---

## Next Steps

- Jika terjadi issue saat compile, lihat [README_BUILD.md](README_BUILD.md)
- Untuk menjalankan di emulator, lihat [README_EMULATOR.md](README_EMULATOR.md)
- Untuk running tests, lihat [README_TESTS.md](README_TESTS.md)
