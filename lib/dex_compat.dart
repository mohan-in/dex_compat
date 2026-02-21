import 'package:flutter/material.dart';

import 'dex_compat_platform_interface.dart';

/// A utility class for Samsung DeX / desktop windowing compatibility.
///
/// Usage:
/// ```dart
/// final isDesktop = await DexCompat.isDesktopMode();
/// runApp(
///   MaterialApp(
///     builder: DexCompat.builder(isDesktop),
///   ),
/// );
/// ```
class DexCompat {
  DexCompat._();

  /// Returns `true` if the app is running in a desktop windowing environment
  /// such as Samsung DeX (detected via captionBar insets).
  static Future<bool> isDesktopMode() {
    return DexCompatPlatform.instance.isDesktopMode();
  }

  /// Returns a [TransitionBuilder] for [MaterialApp.builder] that strips the
  /// erroneous top padding reported by desktop windowing environments.
  ///
  /// Pass the result of [isDesktopMode] to gate the override so it only
  /// applies when the app is actually running in a desktop window.
  static TransitionBuilder builder(bool isDesktopMode) {
    return (BuildContext context, Widget? child) {
      if (isDesktopMode && child != null) {
        final mediaQuery = MediaQuery.of(context);
        if (mediaQuery.padding.top > 0) {
          return MediaQuery(
            data: mediaQuery.copyWith(
              padding: mediaQuery.padding.copyWith(top: 0),
            ),
            child: child,
          );
        }
      }
      return child ?? const SizedBox.shrink();
    };
  }
}
