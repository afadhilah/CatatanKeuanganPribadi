# FAQ - Pertanyaan yang Sering Diajukan

---

## Penggunaan Aplikasi

### Q: Bagaimana cara mulai menggunakan aplikasi?

**A:** 
1. Download APK dari GitHub Releases
2. Install di Android device (API 24+)
3. Buat akun pertama dengan nominal awal
4. Mulai catat transaksi dari dashboard

Detail: [Installation Guide](Installation.md)

---

### Q: Saya lupa mencatat transaksi kemarin. Bagaimana cara mengedit?

**A:**
1. Buka **Transactions** menu
2. Cari transaksi yang ingin di-edit
3. Tap untuk view detail
4. Tap **Edit** button
5. Ubah data sesuai kebutuhan
6. Tap **Save**

---

### Q: Bisakah saya recover transaksi yang sudah dihapus?

**A:**
Jika Anda memiliki backup, bisa restore:
1. Settings → Data Management → Restore
2. Pilih file backup
3. Konfirmasi restore

**Catatan:** Transaksi yang sudah dihapus tanpa backup tidak bisa di-recover. Selalu backup data regularly.

---

### Q: Bagaimana cara transfer uang antar akun?

**A:**
1. Tap **+** button (FAB) di dashboard
2. Pilih **Transfer**
3. Pilih akun sumber dan tujuan
4. Input jumlah transfer
5. Tap **Save**

Transfer akan tercatat sebagai **debit di akun sumber** dan **kredit di akun tujuan**.

---

### Q: Apakah aplikasi online atau offline?

**A:**
Aplikasi berjalan **offline** - semua data tersimpan lokal di device. Cloud sync masih dalam roadmap untuk versi mendatang.

---

## Fitur & Kalkulasi

### Q: Bagaimana cara budget bekerja?

**A:**
1. Set budget limit per kategori di menu **Budget**
2. Setiap transaksi expense akan dikurangi dari limit
3. App akan alert ketika:
   - 70% dari budget terpakai
   - Sudah exceed budget

---

### Q: Mengapa saldo saya tidak sesuai dengan catatan saya?

**A:**
Kemungkinan:
1. Ada transaksi yang tidak tercatat
2. Transfer tidak tercatat dengan benar
3. Edit/delete transaksi menyebabkan inconsistency

**Solution:**
1. Check filter di transaction list (mungkin ada yang di-filter)
2. View semua transaksi tanpa filter
3. Manual recount dan reconcile

---

### Q: Bisakah saya mengubah mata uang?

**A:**
Ya, di Settings:
1. Settings → Display → Currency
2. Pilih mata uang yang diinginkan
3. Semua nominal akan show dengan currency yang baru

**Catatan:** Currency hanya untuk display, tidak otomatis convert.

---

## Technical

### Q: Bagaimana cara backup dan restore data?

**A:**

**Backup:**
1. Settings → Data Management → Backup Now
2. File backup akan tersimpan
3. Share via email/cloud jika diinginkan

**Restore:**
1. Settings → Data Management → Restore
2. Pilih file backup
3. Konfirmasi restore (akan overwrite current data)

---

### Q: Bagaimana cara export data ke format lain?

**A:**
1. Settings → Data Management → Export
2. Pilih format:
   - **CSV** - Untuk Excel/Spreadsheet
   - **JSON** - Untuk developer/import ke app lain
3. File akan di-generate dan bisa di-share

---

### Q: Bisakah saya import data dari aplikasi lain?

**A:**
Jika aplikasi lain support export ke format yang compatible:
1. Export data dari app lain (CSV atau JSON)
2. Settings → Data Management → Import
3. Pilih file yang di-export
4. Konfirmasi import

---

### Q: Apakah aplikasi aman untuk data keuangan saya?

**A:**
Security features:
- **PIN/Password protection** (optional)
- **Biometric authentication** (fingerprint/face)
- **Local encryption** untuk database
- **Tidak ada upload ke server** (offline)
- **No ads atau tracking**

**Best Practice:**
- Set PIN/Biometric protection
- Regular backup
- Keep Android updated

---

## Dashboard & Analytics

### Q: Bagaimana cara view statistik pengeluaran?

**A:**
1. Menu → **Statistics**
2. Pilih periode (This Month, Last 3M, This Year, Custom)
3. Lihat berbagai chart:
   - Pie chart - Proporsi per kategori
   - Bar chart - Monthly trends
   - Line chart - Balance trend

---

### Q: Bisa export laporan dalam format PDF?

**A:**
Ya, via export:
1. Statistics → Menu (3 dots)
2. Tap **Export**
3. Pilih format PDF
4. File akan di-generate

---

## Troubleshooting

### Q: Aplikasi force close ketika membuka tertentu

**A:**
1. **Clear Cache:**
   - Android Settings → Apps → CatatanKeuangan → Storage → Clear Cache
   
2. **Restart Device:**
   - Restart Android device
   
3. **Reinstall:**
   - Uninstall aplikasi
   - Download APK terbaru dari GitHub
   - Install ulang

4. **Check Version:**
   - Pastikan Android version compatible (API 24+)

---

### Q: Data tidak muncul setelah update

**A:**
1. **Refresh:** Swipe down untuk refresh
2. **Check Filter:** Mungkin ada date/category filter aktif
3. **Restart App:** Close dan buka ulang aplikasi
4. **Restore Backup:** Jika data hilang, restore dari backup

---

### Q: Notifikasi budget tidak muncul

**A:**
1. **Check Permission:**
   - Android Settings → Apps → CatatanKeuangan → Notifications
   - Pastikan "All" atau notifications specific permission aktif

2. **Check Setting:**
   - App Settings → Notifications → Budget Alerts
   - Pastikan toggle aktif (ON)

3. **Check Status:**
   - Pastikan actual expense sudah mencapai 70% dari budget

---

### Q: Backup/Restore gagal

**A:**
1. **Check Storage:**
   - Pastikan device punya space cukup

2. **Check Permission:**
   - Android Settings → Apps → CatatanKeuangan → Permissions
   - Ensure file access permission granted

3. **File Format:**
   - Ensure file format supported (JSON, CSV)

4. **Update App:**
   - Download version terbaru

---

## Update & Versioning

### Q: Bagaimana cara update aplikasi?

**A:**
1. Check [Releases page](../../releases) untuk version terbaru
2. Download APK baru
3. Install (akan replace old version, data tetap)

---

### Q: Apakah ada changelog untuk setiap update?

**A:**
Ya, di setiap release:
1. Buka [Releases](../../releases)
2. Klik release tertentu
3. Lihat release notes dengan:
   - **Features added**
   - **Bugs fixed**
   - **Changes**
   - **Breaking changes** (jika ada)

---

### Q: Bagaimana compatibility dengan Android version baru?

**A:**
- Minimal Android: 7.0 (API 24)
- Target Android: Android 16 (API 36)
- Tested dengan berbagai Android versions

Check [Development Setup](Development-Setup.md) untuk requirement detail.

---

## Developer Questions

### Q: Bagaimana cara contribute ke project ini?

**A:**
1. Fork repository
2. Create feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m "feat: add your feature"`
4. Push: `git push origin feature/your-feature`
5. Create Pull Request

Detail: [Contributing Guide](Contributing.md)

---

### Q: Bagaimana struktur project?

**A:**
Project menggunakan:
- **Clean Architecture** - Separation of concerns
- **MVVM Pattern** - Presentation layer
- **Room Database** - Local storage
- **Jetpack Compose** - UI Framework

Detail: [Architecture](Architecture.md)

---

### Q: Bagaimana setup development environment?

**A:**
1. Install JDK 21 (important! not 25)
2. Clone repository
3. Create `local.properties` dengan Android SDK path
4. Run `./gradlew build`

Detail: [Development Setup](Development-Setup.md)

---

### Q: Bagaimana run tests?

**A:**
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Code coverage
./gradlew testDebugUnitTestCoverage
```

---

### Q: Bagaimana build APK?

**A:**
```bash
# Debug APK (for testing)
./gradlew assembleDebug

# Release APK (for production)
./gradlew assembleRelease

# AAB (for Google Play Store)
./gradlew bundleRelease
```

Output akan ada di `app/build/outputs/`

---

## 📞 Support & Feedback

### Q: Bagaimana cara report bug?

**A:**
1. Buka [GitHub Issues](../../issues)
2. Click "New Issue"
3. Describe bug dengan detail:
   - What happened?
   - Expected behavior?
   - Steps to reproduce?
   - Device/Android version?
4. Submit

---

### Q: Bagaimana cara request feature baru?

**A:**
1. Buka [GitHub Discussions](../../discussions) atau [Issues](../../issues)
2. Describe feature dengan detail:
   - What feature do you want?
   - Why is it useful?
   - How should it work?
3. Discuss dengan community

---

### Q: Bagaimana cara contact developer?

**A:**
- 📧 Email: [support@catatankeuangan.com]
- 💬 GitHub Discussions: [Link](../../discussions)
- 🐛 Issue Tracker: [Link](../../issues)

---

## Additional Resources

- [Installation & User Guide](Installation.md)
- [Features Detail](Features.md)
- [Architecture Guide](Architecture.md)
- [Development Setup](Development-Setup.md)
- [Contributing Guide](Contributing.md)
- [GitHub Actions Guide](GitHub-Actions.md)

---

**Last Updated**: April 29, 2026

---

**Tidak menemukan jawaban?** Create issue atau discussion di GitHub!
