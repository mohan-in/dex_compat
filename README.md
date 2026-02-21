# dex_compat

A Flutter plugin for Samsung DeX and Android desktop windowing compatibility.

Detects when your app is running inside a desktop window (e.g. Samsung DeX, freeform windowing on large-screen Android devices) and provides a `MaterialApp` builder that removes the erroneous top padding reported by the desktop window's caption bar.

---

## Features

- Detects Samsung DeX and freeform desktop windowing via `captionBar` window insets (Android 11+).
- Falls back to multi-window + large-screen detection for Android 10 and below.
- Provides a `TransitionBuilder` for `MaterialApp.builder` that zeroes out the phantom top padding only when running in a desktop window.
- Android-only — no-op on other platforms.

---

## Installation

Add the plugin as a path dependency (until it is published to pub.dev):

```yaml
# pubspec.yaml
dependencies:
  dex_compat:
    path: ../packages/dex_compat
```

Then run:

```bash
flutter pub get
```

---

## Usage

### 1. Detect desktop mode and apply the builder

```dart
import 'package:dex_compat/dex_compat.dart';
import 'package:flutter/material.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final bool isDesktop = await DexCompat.isDesktopMode();

  runApp(
    MaterialApp(
      builder: DexCompat.builder(isDesktop),
      home: const MyHomePage(),
    ),
  );
}
```

### 2. Check desktop mode at runtime

```dart
final bool isDesktop = await DexCompat.isDesktopMode();
if (isDesktop) {
  // Adjust layout for a desktop window
}
```

---

## API

### `DexCompat.isDesktopMode() → Future<bool>`

Returns `true` if the app is currently running in a desktop windowing environment.

**Detection strategy (in priority order):**

| Check | Android version | Description |
|---|---|---|
| `captionBar` inset | Android 11+ (API 30+) | Checks for a non-zero `WindowInsets.Type.captionBar()` top inset. This is the definitive signal for a DeX title bar. |
| Multi-window + large screen | All versions | Falls back to `isInMultiWindowMode && smallestScreenWidthDp >= 600` for freeform windowing on tablets/foldables. |

### `DexCompat.builder(bool isDesktopMode) → TransitionBuilder`

Returns a `TransitionBuilder` suitable for `MaterialApp.builder`. When `isDesktopMode` is `true`, it wraps the child in a `MediaQuery` that clears the top padding, preventing layouts from being incorrectly offset by the caption bar's height.

---

## Platform support

| Platform | Support |
|---|---|
| Android | ✅ |
| iOS | ❌ |
| Web | ❌ |
| macOS / Windows / Linux | ❌ |

---

## Requirements

- Flutter `>=3.3.0`
- Dart `^3.10.4`
- Android `minSdkVersion` — no additional requirement beyond your app's existing minimum. The caption-bar check is gated behind an `Build.VERSION.SDK_INT >= Build.VERSION_CODES.R` guard.
