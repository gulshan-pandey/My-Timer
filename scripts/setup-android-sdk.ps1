# Minimal Android SDK for command-line builds from Cursor (Windows).
$ErrorActionPreference = "Stop"

$SdkRoot = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { Join-Path $env:LOCALAPPDATA "Android\Sdk" }
$CmdLineDir = Join-Path $SdkRoot "cmdline-tools\latest"
$SdkManager = Join-Path $CmdLineDir "bin\sdkmanager.bat"

Write-Host "SDK root: $SdkRoot"

New-Item -ItemType Directory -Force -Path $SdkRoot | Out-Null

if (-not (Test-Path $SdkManager)) {
    $zip = Join-Path $env:TEMP "commandlinetools-win-latest.zip"
    $extract = Join-Path $env:TEMP "android-cmdline-tools"
    Write-Host "Downloading Android command-line tools (may take a few minutes)..."
    $url = "https://dl.google.com/android/repository/commandlinetools-win-13114758_latest.zip"
    $maxAttempts = 5
    for ($attempt = 1; $attempt -le $maxAttempts; $attempt++) {
        try {
            Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing
            break
        } catch {
            if ($attempt -eq $maxAttempts) { throw }
            Write-Host "Download failed (attempt $attempt). Retrying in 10s..."
            Start-Sleep -Seconds 10
        }
    }
    if (Test-Path $extract) { Remove-Item $extract -Recurse -Force }
    Expand-Archive -Path $zip -DestinationPath $extract -Force
    New-Item -ItemType Directory -Force -Path (Join-Path $SdkRoot "cmdline-tools") | Out-Null
    $destLatest = Join-Path $SdkRoot "cmdline-tools\latest"
    if (Test-Path $destLatest) { Remove-Item $destLatest -Recurse -Force }
    Move-Item (Join-Path $extract "cmdline-tools") $destLatest
}

Write-Host "Installing platform-tools, Android 35 platform, build-tools..."
$packages = @(
    "platform-tools",
    "platforms;android-35",
    "build-tools;35.0.0"
)
foreach ($pkg in $packages) {
    & $SdkManager --sdk_root=$SdkRoot $pkg | Out-Host
}

Write-Host "Accepting SDK licenses..."
1..100 | ForEach-Object { "y" } | & $SdkManager --sdk_root=$SdkRoot --licenses

$localProps = Join-Path $PSScriptRoot "..\local.properties"
"sdk.dir=$($SdkRoot -replace '\\','/')" | Set-Content -Path $localProps -Encoding ASCII

Write-Host ""
Write-Host "Done. Wrote local.properties"
Write-Host "Optional: set user env ANDROID_HOME=$SdkRoot"
Write-Host "Then from project root: .\gradlew.bat :app:assembleDebug"
