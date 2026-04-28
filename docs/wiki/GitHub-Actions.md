# GitHub Actions - CI/CD Pipeline

Dokumentasi lengkap tentang GitHub Actions yang digunakan untuk build dan release aplikasi.

---

## Overview

Project ini menggunakan GitHub Actions untuk automasi:
- **Compile** aplikasi setiap ada push
- **Generate APK/AAB** build artifacts
- **Create Release** dengan automatic tag
- **Upload artifacts** ke GitHub Release

---

## Workflows

### 1. **Build and Release APK** (`build-and-release.yml`)

Workflow utama untuk compile dan release aplikasi.

#### Triggers:
```yaml
on:
  push:
    branches:
      - main
      - master
      - develop
    tags:
      - 'v*'
  workflow_dispatch:
```

**Trigger Events:**
- Push ke branch: `main`, `master`, `develop`
- Push tag dengan format `v*` (contoh: `v1.0.0`)
- ✅ Manual trigger via workflow_dispatch button

#### Jobs:

1. **Checkout Code**
   ```
   Clones repository dengan git history
   ```

2. **Setup Java 21**
   ```
   Install JDK 21 (required untuk Android SDK)
   Enable Gradle caching untuk faster builds
   ```

3. **Setup Android SDK**
   ```
   Download dan setup Android SDK
   Install required build tools
   ```

4. **Accept Android Licenses**
   ```
   Automatically accept Android SDK licenses
   ```

5. **Build Release APK**
   ```
   ./gradlew assembleRelease --no-daemon
   Output: app/build/outputs/apk/release/app-release.apk
   ```

6. **Build Release AAB**
   ```
   ./gradlew bundleRelease --no-daemon
   Output: app/build/outputs/bundle/release/app-release.aab
   ```

7. **Create GitHub Release**
   ```
   Buat release dengan tag
   Upload APK dan AAB sebagai assets
   Generate release notes
   ```

#### Outputs:
- 📦 **APK**: `app-release.apk` (untuk manual install)
- 📦 **AAB**: `app-release.aab` (untuk Google Play Store)
- 📝 **Release Notes**: Auto-generated dari commits

---

### 2. **Build Debug APK** (`build-debug.yml`)

Workflow untuk compile debug APK di setiap push (untuk testing).

#### Triggers:
```yaml
on:
  push:
    branches:
      - main
      - master
      - develop
      - '**'
  pull_request:
```

**Trigger Events:**
- **Push ke semua branch**
- **Pull Request**

#### Jobs:

1. Setup Java & Android SDK (sama seperti di atas)

2. **Build Debug APK**
   ```
   ./gradlew assembleDebug --no-daemon
   Output: app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Upload as Artifact**
   ```
   Upload ke GitHub Artifacts
   Retention: 7 hari
   ```

#### Outputs:
- **Debug APK**: `app-debug.apk` (untuk testing)
- **Location**: GitHub Actions → Artifacts tab

---

## Penggunaan

### Scenario 1: Push Biasa (Testing)

```bash
git add .
git commit -m "fix: bug in transaction"
git push origin main
```

**Hasil:**
1. GitHub Action otomatis trigger
2. Build debug APK
3. APK tersimpan 7 hari di Artifacts

**Download APK:**
- Buka GitHub → Actions tab
- Klik workflow terbaru
- Scroll ke Artifacts
- Download `debug-apk-xxx`

---

### Scenario 2: Release (Production)

```bash
# Pastikan code sudah siap
git add .
git commit -m "chore: release v1.0.0"

# Create tag
git tag v1.0.0

# Push code dan tag
git push origin main
git push origin v1.0.0
```

**Hasil:**
1. GitHub Action trigger dengan tag detection
2. Build release APK + AAB
3. Create GitHub Release dengan tag `v1.0.0`
4. Upload APK + AAB ke Release Assets
5. Generate release notes dari commits

**Download APK:**
- Buka GitHub → Releases tab
- Klik release `v1.0.0`
- Download `app-release.apk` atau `app-release.aab`

---

## Workflow Status

### Check Workflow Status

1. **Buka GitHub Repository**
2. **Klik tab Actions**
3. Lihat list workflow runs terbaru

### Status Indicators:
- **Success** - Build berhasil
- **Failed** - Ada error di build
- **In Progress** - Sedang build
- **Queued** - Menunggu untuk run

### View Logs

1. Klik workflow run
2. Klik job → Expand steps
3. Lihat logs untuk debugging

---

## Artifacts Management

### Debug APK Artifacts
- **Location**: Actions tab → Artifacts
- **Retention**: 7 hari
- **Size**: ~50-100 MB

### Download via API:
```bash
# Dapatkan list artifacts
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/repos/afadh/CatatanKeuanganPribadi/actions/artifacts

# Download artifact
curl -H "Authorization: token YOUR_TOKEN" \
  -L https://api.github.com/repos/afadh/CatatanKeuanganPribadi/actions/artifacts/{id}/zip \
  -o artifact.zip
```

---

## Permissions & Security

Workflow menggunakan `GITHUB_TOKEN` dengan permissions:
```yaml
permissions:
  contents: write    # Create releases
  packages: write    # Push packages
```

**Security Note:**
- Token hanya valid untuk current workflow run
- Tidak tersimpan atau di-log
- Auto-rotate setelah setiap run

---

## Configuration

### Customize Workflow

Edit file `.github/workflows/build-and-release.yml`:

#### Ubah Trigger Branch:
```yaml
on:
  push:
    branches:
      - main
      - develop
      - feature/*  # Tambah pattern
```

#### Ubah Android SDK Version:
```yaml
- name: Setup Android SDK
  with:
    api-level: 36
    ndk-version: 26.0.0
```

#### Ubah Release Type:
```yaml
- name: Build Release APK
  run: ./gradlew assembleRelease --no-daemon
  # Atau ubah ke:
  # run: ./gradlew assemble<BuildType> --no-daemon
```

---

## Troubleshooting

### Error: JDK Version Mismatch
```
This tool requires JDK 17 or later. Your version was detected as 11.0.30
```
**Solution:** Workflow sudah updated ke Java 21. Jika masih error, restart runner.

### Error: APK Pattern Not Found
```
Pattern 'app/build/outputs/apk/release/app-release.apk' does not match any files
```
**Solution:** Check:
1. Build type setting di `build.gradle.kts`
2. Output path di workflow
3. Build log untuk error message

### Error: GitHub Token Permission
```
Resource not accessible by integration
```
**Solution:** Permissions sudah di-set di workflow. Jika masih gagal:
1. Check repository settings
2. Verify token permissions
3. Ensure `contents: write` permission active

### Build Timeout
```
Build took too long
```
**Solution:**
1. Check build log untuk bottleneck
2. Enable Gradle caching (sudah ada)
3. Parallel build: `org.gradle.parallel=true` (sudah di-config)

---

## 📊 Build Performance

### Current Performance:
- **First Build**: ~10-15 menit (download SDK)
- **Subsequent Builds**: ~5-7 menit (cached)
- **Upload Release**: ~1-2 menit

### Optimization Tips:
1. Gradle caching enabled ✓
2. Parallel builds enabled ✓
3. Only build changed modules
4. Use prebuilt tools

---

## 🔄 Best Practices

### 1. **Semantic Versioning**
```bash
git tag v1.0.0    # Major.Minor.Patch
git tag v1.0.1
git tag v2.0.0
```

### 2. **Meaningful Commit Messages**
```
feat: add transaction filter
fix: fix budget calculation bug
chore: update dependencies
```

Commits digunakan untuk auto-generate release notes.

### 3. **Branch Strategy**
- `main` - Production releases
- `develop` - Development builds
- `feature/*` - Feature branches

### 4. **Test Before Release**
```bash
# Build locally terlebih dahulu
./gradlew clean build

# Test di emulator
./gradlew installDebug

# Jika OK, baru tag dan push
```

---

## 📝 Release Notes Template

Auto-generated release notes akan include:
- 🎉 **Features** - Fitur baru
- 🐛 **Bug Fixes** - Bug yang di-fix
- 📚 **Documentation** - Doc changes
- 🔄 **Chores** - Maintenance tasks

---

## 🔗 Resources

- [GitHub Actions Docs](https://docs.github.com/actions)
- [Android GitHub Actions](https://github.com/android-actions)
- [Gradle on GitHub Actions](https://github.com/gradle/gradle-build-action)

---

## 📞 Support

- 🐛 Report workflow issues: [GitHub Issues](../../issues)
- 💬 Discussion: [GitHub Discussions](../../discussions)

---

**Last Updated**: April 29, 2026  
**Workflow Version**: 2.0
