$ErrorActionPreference = "Stop"

# Locate Android SDK
$sdk = $env:ANDROID_HOME
if (-not $sdk) { $sdk = $env:ANDROID_SDK_ROOT }
if (-not $sdk) { $sdk = Join-Path $env:LOCALAPPDATA "Android\Sdk" }

if (-not (Test-Path $sdk)) {
    Write-Error "Android SDK not found. Set ANDROID_HOME or install it at $sdk"
    exit 1
}

$adb = Join-Path $sdk "platform-tools\adb.exe"
$emulator = Join-Path $sdk "emulator\emulator.exe"
$packageName = "com.example.teammate"

# Check if a device/emulator is already online
$devices = & $adb devices | Select-String "\tdevice$"
if (-not $devices) {
    $avds = & $emulator -list-avds
    if (-not $avds) {
        Write-Error "No Android Virtual Devices (AVDs) found. Create one in Android Studio Device Manager first."
        exit 1
    }

    $avdName = ($avds | Select-Object -First 1).Trim()
    Write-Host "Starting emulator: $avdName..." -ForegroundColor Cyan
    Start-Process -FilePath $emulator -ArgumentList "-avd", $avdName -WindowStyle Normal

    Write-Host "Waiting for emulator to boot..." -ForegroundColor Cyan
    & $adb wait-for-device | Out-Null
    while ((& $adb shell getprop sys.boot_completed 2>$null).Trim() -ne "1") {
        Start-Sleep -Seconds 2
    }
    Write-Host "Emulator is ready." -ForegroundColor Green
} else {
    Write-Host "Using already connected device/emulator." -ForegroundColor Green
}

# Build and install debug APK
Write-Host "Building and installing debug APK..." -ForegroundColor Cyan
& "$PSScriptRoot\gradlew.bat" installDebug
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

# Launch the app
Write-Host "Launching $packageName..." -ForegroundColor Cyan
& $adb shell monkey -p $packageName -c android.intent.category.LAUNCHER 1 | Out-Null
Write-Host "Done!" -ForegroundColor Green
