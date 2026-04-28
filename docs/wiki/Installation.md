# Instalasi & User Guide

Panduan lengkap untuk install dan menggunakan CatatanKeuanganPribadi.

---

## Instalasi Aplikasi

### Opsi 1: Download APK dari GitHub Release (Recommended)

1. **Buka GitHub Repository**
   - Kunjungi: https://github.com/afadh/CatatanKeuanganPribadi

2. **Navigasi ke Releases**
   - Klik tab **Releases** di sidebar kanan

3. **Download APK**
   - Pilih release terbaru (contoh: v1.0.0)
   - Download file `app-release.apk`

4. **Transfer ke Android Device**
   ```bash
   adb push app-release.apk /sdcard/Download/
   ```

5. **Install via File Manager**
   - Buka file manager di Android
   - Navigasi ke Downloads
   - Tap file APK
   - Tap "Install"

---

### Opsi 2: Build & Install dari Source

```bash
# 1. Clone repository
git clone https://github.com/afadh/CatatanKeuanganPribadi.git
cd CatatanKeuanganPribadi

# 2. Build APK
./gradlew assembleRelease

# 3. Install ke device
./gradlew installRelease

# Atau manual:
adb install app/build/outputs/apk/release/app-release.apk
```

---

## ⚙️ Requirements Sistem

- **Android Version**: Android 7.0+ (API 24+)
- **Storage**: Min 50 MB free space
- **RAM**: Min 2 GB
- **Internet**: Opsional (for cloud features)

---

## Setup Awal Aplikasi

### 1. **Buka Aplikasi**
   - Tap app icon di home screen
   - Aplikasi akan membuka halaman welcome/onboarding

### 2. **Setup Awal** (jika ada)
   - Pilih mata uang (Rp, $, €, dll)
   - Pilih preferensi bahasa
   - Arahkan biometric login (fingerprint/face)

### 3. **Buat Akun Pertama**
   - Menu → Accounts
   - Tap tombol "+" (Add Account)
   - Input:
     - **Nama Akun**: Contoh "Dompet Saya"
     - **Jenis**: Cash / Bank / E-Wallet
     - **Saldo Awal**: Jumlah uang saat ini
   - Tap "Save"

---

## Penggunaan Aplikasi

### 1. **Mencatat Transaksi Pertama**

#### Dari Dashboard:
1. Tap tombol floating action button (FAB) dengan icon "+"
2. Pilih jenis transaksi:
   - **Income** - Jika menerima uang
   - **Expense** - Jika mengeluarkan uang
   - **Transfer** - Transfer antar akun

#### Form Transaksi:
- **Tanggal**: Tap untuk pilih tanggal
- **Kategori**: Pilih kategori (Makanan, Transportasi, dll)
- **Akun**: Pilih akun sumber
- **Jumlah**: Input nominal uang
- **Deskripsi**: Keterangan (optional)
- Tap **Save**

---

### 2. **Melihat Transaksi**

#### List Transaksi:
1. Menu → Transactions
2. Lihat daftar semua transaksi

#### Filter Transaksi:
- **Filter by Date**: Tap calendar icon, pilih range tanggal
- **Filter by Category**: Tap category icon, pilih kategori
- **Filter by Account**: Tap akun, pilih account
- **Search**: Gunakan search bar

#### Detail Transaksi:
1. Tap transaksi di list
2. Lihat detail lengkap
3. Pilih Edit atau Delete

---

### 3. **Mengelola Akun**

#### View Semua Akun:
1. Menu → Accounts
2. Lihat daftar semua akun dengan saldo

#### Tambah Akun Baru:
1. Menu → Accounts
2. Tap tombol "+"
3. Input informasi akun
4. Tap Save

#### Edit Akun:
1. Menu → Accounts
2. Long-press akun yang mau di-edit
3. Tap Edit
4. Ubah informasi
5. Tap Save

#### Hapus Akun:
1. Menu → Accounts
2. Long-press akun yang mau di-hapus
3. Tap Delete
4. Konfirmasi (jika ada transaksi, mungkin tidak bisa dihapus)

---

### 4. **Setup Budget**

#### Create Budget:
1. Menu → Budget
2. Tap tombol "+"
3. Input:
   - **Kategori**: Pilih kategori pengeluaran
   - **Limit**: Jumlah budget
   - **Periode**: Bulanan / Custom
4. Tap Save

#### Monitor Budget:
1. Menu → Budget
2. Lihat progress bar untuk setiap kategori
3. Warna akan berubah jika mendekati/melampaui limit:
   - **Hijau**: < 70% dari budget
   - **Kuning**: 70-100% dari budget
   - **Merah**: > 100% (exceed budget)

#### Edit/Delete Budget:
1. Menu → Budget
2. Tap budget yang mau di-edit
3. Ubah atau hapus

---

### 5. **Lihat Dashboard**

#### Dashboard Overview:
1. Buka aplikasi (default ke dashboard)
2. Lihat:
   - **Total saldo** dari semua akun
   - **Income bulan ini**
   - **Expense bulan ini**
   - **Pie chart** pengeluaran per kategori

#### Top Actions:
- Tap chart untuk detail breakdown
- Swipe untuk navigate ke tab lain
- Tap account untuk detail transaksi akun tertentu

---

### 6. **Analisis Pengeluaran**

#### View Statistics:
1. Menu → Statistics
2. Pilih periode:
   - **This Month**
   - **Last 3 Months**
   - **This Year**
   - **Custom Range**

#### Chart Types:
- **Pie Chart**: Proporsi pengeluaran per kategori
- **Bar Chart**: Tren monthly
- **Line Chart**: Trend balance over time

#### Export Report:
1. Menu → Statistics
2. Tap menu (3 dots)
3. Pilih Export:
   - CSV
   - PDF
   - Email

---

## ⚙️ Pengaturan Aplikasi

### Buka Settings:
1. Menu → Settings

### Opsi Pengaturan:

#### Display:
- **Theme**: Light / Dark / Auto
- **Language**: Pilih bahasa
- **Currency**: Pilih mata uang

#### Notifications:
- **Budget Alerts**: Aktif/Nonaktif
- **Daily Reminder**: Aktif/Nonaktif
- **Weekly Summary**: Aktif/Nonaktif

#### Security:
- **PIN/Password**: Setup protection
- **Biometric**: Enable fingerprint/face
- **Auto-lock**: Waktu sebelum auto-lock

#### Data Management:
- **Auto-backup**: Enable/disable
- **Backup Now**: Manual backup
- **Restore**: Restore dari backup
- **Export Data**: Export ke CSV/JSON
- **Import Data**: Import dari file

#### About:
- App version
- Developer info
- Support/Feedback

---

## 💡 Tips & Tricks

### 1. **Kategori Custom**
   - Anda bisa menambah kategori custom di Settings
   - Category akan tersimpan untuk penggunaan selanjutnya

### 2. **Quick Entry**
   - Gunakan search bar untuk quick transaksi input
   - Shortcut dengan suara (voice input) jika tersedia

### 3. **Backup Regular**
   - Backup data secara regular untuk aman
   - Upload backup ke cloud (jika available)

### 4. **Analisis Pattern**
   - Review statistik bulanan untuk understand spending pattern
   - Adjust budget berdasarkan actual spending

### 5. **Multiple Accounts**
   - Separate cash, bank, dan e-wallet untuk tracking lebih akurat
   - Transfer antar akun untuk balance management

---

## 🐛 Troubleshooting

### App Force Close
**Solution:**
1. Clear app cache: Settings → Apps → CatatanKeuangan → Clear Cache
2. Uninstall dan reinstall aplikasi
3. Check Android version compatibility

### Data Tidak Muncul
**Solution:**
1. Refresh: Swipe down untuk refresh
2. Check filter (mungkin di-filter tanggal/kategori)
3. Restart aplikasi

### Budget Alert Tidak Muncul
**Solution:**
1. Check notification permission: Settings → Notifications → CatatanKeuangan
2. Enable budget alerts di Settings
3. Check jika budget sudah melebihi limit

### Backup/Restore Gagal
**Solution:**
1. Check storage space
2. Check file permission
3. Update app ke versi terbaru

---

## 📞 Bantuan & Support

- 📧 Email: [support@catatankeuangan.com]
- 🐛 Report Issues: https://github.com/afadh/CatatanKeuanganPribadi/issues
- 💬 Discussion: https://github.com/afadh/CatatanKeuanganPribadi/discussions
- Feedback: In-app feedback form

---

## 🎓 Video Tutorials

Kunjungi YouTube channel kami untuk tutorial lengkap:
- Setup awal
- Recording transactions
- Budget management
- Advanced features

---

**Last Updated**: April 29, 2026  
**Version**: 1.0.0
