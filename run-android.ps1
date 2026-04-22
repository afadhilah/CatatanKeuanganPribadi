$ErrorActionPreference = "Stop"

$adb = Join-Path $env:LOCALAPPDATA "Android\Sdk\platform-tools\adb.exe"
$emulator = Join-Path $env:LOCALAPPDATA "Android\Sdk\emulator\emulator.exe"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CatatanKeuanganPribadi - Emulator Build" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/4] Starting emulator..." -ForegroundColor Yellow
$avd = "Medium_Phone_API_36.0"
& $emulator -avd $avd -no-snapshot-load *>$null &
Write-Host "  Waiting for emulator to boot..." -ForegroundColor Gray
& $adb wait-for-device
Start-Sleep -Seconds 3
Write-Host "✓ Emulator ready" -ForegroundColor Green

Write-Host ""
Write-Host "[2/4] Building debug APK..." -ForegroundColor Yellow
& .\gradlew.bat :app:assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Build successful" -ForegroundColor Green

Write-Host ""
Write-Host "[3/4] Installing APK on emulator..." -ForegroundColor Yellow
& .\gradlew.bat :app:installDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Installation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Installation successful" -ForegroundColor Green

Write-Host ""
Write-Host "[4/4] Launching app..." -ForegroundColor Yellow
& $adb shell monkey -p com.example.catatankeuanganpribadi -c android.intent.category.LAUNCHER 1

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  ✓ App launched on emulator!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan