# My Timer

My Timer is an Android app that displays how many seconds remain until midnight in the phone's local time zone. It includes a home-screen widget and an ongoing notification.

## Features

- Live countdown in seconds on the app screen
- Home-screen widget that refreshes the countdown every second while the app's foreground service is running
- Optional ongoing countdown notification
- Midnight refresh and reboot handling

## Requirements

- JDK 17
- Android SDK Platform 35 and Android SDK Build-Tools 35.0.0
- A phone or emulator running Android 8.0 (API 26) or newer to run the app

You can install the Android SDK with Android Studio, or use the Windows setup script included in this repository. Android Studio is not required to build from the command line.

## Open in VS Code

1. Clone or download this repository and open its folder in VS Code.
2. Open the integrated terminal at the project root.
3. Make sure Java 17 is installed and available on your `PATH`:

   ```powershell
   java -version
   ```

4. Set up the Android SDK:

   On Windows, run the included setup script in PowerShell. It downloads the command-line tools, installs the required SDK packages, accepts the SDK licenses, and writes `local.properties` for your machine.

   ```powershell
   powershell -ExecutionPolicy Bypass -File .\scripts\setup-android-sdk.ps1
   ```

   On macOS or Linux, install Android SDK Platform 35 and Build-Tools 35.0.0 using Android Studio's **SDK Manager** or the Android command-line tools. Make sure Gradle can find the SDK, either by opening the project in Android Studio to generate `local.properties` or by setting the `ANDROID_HOME` environment variable.

`local.properties` contains a machine-specific SDK path. Do not commit it; the repository includes `local.properties.example` as a reference.

## Build and Test

Run these commands from the project root.

Build a debug APK on Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

On macOS or Linux:

```sh
./gradlew :app:assembleDebug
```

The debug APK is created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Run the unit tests (these do not require a connected phone):

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

Use `./gradlew :app:testDebugUnitTest` on macOS or Linux.

## Install and Run on a Phone

### Install over USB

1. On the phone, enable **Developer options**. On most devices, go to **Settings → About phone** and tap **Build number** seven times. Menu names vary by manufacturer.
2. In **Developer options**, turn on **USB debugging**.
3. Connect the phone to the computer with a USB data cable, unlock it, and approve the USB debugging prompt.
4. From the project root, install the debug build:

   ```powershell
   .\gradlew.bat :app:installDebug
   ```

   On macOS or Linux, run `./gradlew :app:installDebug`.

5. Open **My Timer** from the phone's app list. On Android 13 and newer, allow notifications if you want the ongoing countdown notification.
6. To add the widget, long-press an empty area of the home screen, open **Widgets**, and add **My Timer countdown**.

The app starts its foreground service when opened. Keep the service enabled for per-second widget and notification updates; battery-management settings on some phones may stop background services.

### Install the APK manually

Build the APK, transfer `app/build/outputs/apk/debug/app-debug.apk` to the phone, and open it. Android may ask you to allow installation from the app used to open the APK.

## Troubleshooting

- **SDK location not found:** Install the required Android SDK packages and make sure `local.properties` points to your SDK, or set `ANDROID_HOME`. On Windows, rerun `scripts/setup-android-sdk.ps1`.
- **Phone is not detected:** Try a USB data cable, unlock the phone, approve the debugging prompt, and check that USB debugging remains enabled.
- **`adb` is not recognized:** Use Gradle's `:app:installDebug` task, or install Android SDK Platform-Tools and add its `platform-tools` directory to `PATH`.
- **Widget or notification stops updating:** Open My Timer again and check your phone's battery/background restrictions for the app.

## Project Details

- Application ID: `com.mytimer.app`
- Minimum Android version: Android 8.0 (API 26)
- Compile and target SDK: Android 15 (API 35)
- Version: 1.0
