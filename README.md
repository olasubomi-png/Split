# MultiFlex Android

A lightweight, no-root Android helper for background-app troubleshooting, split-screen guidance, and an eFootball gaming checklist tailored to TECNO POP 9.

## What it does
- Opens Android battery optimization settings.
- Opens MultiFlex's app details page so the user can inspect available battery controls.
- Gives TECNO/HiOS-oriented guidance for Auto-start and background activity settings.
- Includes an eFootball gaming checklist and direct entry to Android Settings.
- Adds a **Set up Split Screen for eFootball** shortcut that attempts to open eFootball, then guides the user through HiOS Recent apps.
- Explains how to enter native split-screen mode.

## Honest limitations
- Android does not provide a normal third-party app with a universal "keep every other app alive" switch.
- This app cannot change other apps' battery settings automatically, force unsupported apps into split-screen, or increase RAM. The split-screen shortcut only opens eFootball and provides instructions; the user must select Split screen in HiOS, if supported.
- HiOS menu names and available controls vary by device/software version.
- This initial version intentionally avoids broad app-list permissions, accessibility abuse, background services, and data collection.

## Build APK
Requires JDK 17 and Gradle 8.x / Android Gradle Plugin-compatible environment.

```bash
gradle assembleDebug
```

APK output:
`app/build/outputs/apk/debug/app-debug.apk`

## Build through GitHub Actions
1. Create a GitHub repository and upload this project.
2. Push the files to the `main` branch.
3. Open **Actions → Build MultiFlex APK**.
4. Download the `MultiFlex-debug-apk` artifact from the successful workflow run.
5. Extract the ZIP and install `app-debug.apk` on your Android phone. You may need to allow installation from that source.

The debug APK is for personal testing and is not a Play Store release. For public distribution, create a properly signed release build and review Android/Google Play policy requirements.
