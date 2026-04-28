# Artharum Wiki

Selamat datang di Wiki resmi **Artharum** - Aplikasi Android untuk mencatat dan mengelola keuangan pribadi.

## Tujuan Aplikasi

Aplikasi ini dirancang untuk membantu Anda:
- Mencatat semua transaksi keuangan (income, expense, transfer)
- Mengelola multiple accounts (cash, bank, e-wallet)
- Memantau budget per kategori
- Analisis pengeluaran dengan visualisasi data
- Membuat keputusan finansial yang lebih baik

---

## Dokumentasi Utama

### Panduan Pengguna
- **[Installation](Installation)** - Cara install dan setup aplikasi
- **[Features](Features)** - Penjelasan fitur-fitur aplikasi
- **[User Guide](User-Guide)** - Tutorial penggunaan lengkap
- **[FAQ](FAQ)** - Pertanyaan yang sering diajukan

### Panduan Developer
- **[Architecture](Architecture)** - Arsitektur aplikasi (Clean Architecture + MVVM)
- **[Project Structure](Project-Structure)** - Struktur folder project
- **[Development Setup](Development-Setup)** - Setup environment development
- **[API Integration](API-Integration)** - Integrasi API (jika ada)
- **[Testing](Testing)** - Cara run unit tests dan instrumented tests

### DevOps & Deployment
- **[GitHub Actions](GitHub-Actions)** - CI/CD Pipeline
- **[Build & Release](Build-Release)** - Cara build dan release aplikasi
- **[Troubleshooting](Troubleshooting)** - Pemecahan masalah umum

### Kontribusi
- **[Contributing](Contributing)** - Panduan kontribusi ke project
- **[Code Style](Code-Style)** - Panduan style code Kotlin
- **[Git Workflow](Git-Workflow)** - Workflow git yang digunakan

---

## Quick Start

### Untuk Pengguna
```bash
1. Download APK dari Releases
2. Install di Android device
3. Buka aplikasi dan mulai catat transaksi
```

### Untuk Developer
```bash
# Clone repository
git clone https://github.com/afadh/CatatanKeuanganPribadi.git

# Setup development environment
cd CatatanKeuanganPribadi
./gradlew build

# Run di emulator
./run-android.ps1
```

---

## Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Kotlin | 2.0.21 |
| **Framework** | Jetpack Compose | Latest |
| **Database** | Room | - |
| **Architecture** | Clean Architecture + MVVM | - |
| **Build Tool** | Gradle | 8.13 |
| **AGP** | Android Gradle Plugin | 8.11.2 |
| **Target** | Android 12+ | API 31+ |

---

## Platform

- **OS**: Android
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 36 (Android 16)
- **Compiled SDK**: 36

---

## Support

- Email: [afadhilah2004@gmail.com]
- Issues: Report di [GitHub Issues](../issues)
- Discussions: Join [GitHub Discussions](../discussions)