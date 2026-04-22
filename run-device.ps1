$ErrorActionPreference = "Stop"

$adb = Join-Path $env:LOCALAPPDATA "Android\Sdk\platform-tools\adb.exe"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CatatanKeuanganPribadi - Device Build" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check device
Write-Host "[1/4] Checking connected device..." -ForegroundColor Yellow
$devices = & $adb devices
$deviceCount = ($devices | Measure-Object -Line).Lines - 2
if ($deviceCount -lt 1) {
    Write-Host "ERROR: No device connected!" -ForegroundColor Red
    Write-Host "Please:" -ForegroundColor Red
    Write-Host "  1. Enable USB Debugging on device" -ForegroundColor Red
    Write-Host "  2. Connect device via USB cable" -ForegroundColor Red
    Write-Host "  3. Run this script again" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Device(s) found:" -ForegroundColor Green
$devices | Select-Object -Skip 1

Write-Host ""
Write-Host "[2/4] Building debug APK..." -ForegroundColor Yellow
& .\gradlew.bat :app:assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Build failed!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Build successful" -ForegroundColor Green

Write-Host ""
Write-Host "[3/4] Installing APK on device..." -ForegroundColor Yellow
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
Write-Host "  ✓ App launched on device!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
