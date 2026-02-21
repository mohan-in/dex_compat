import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'dex_compat_platform_interface.dart';

/// An implementation of [DexCompatPlatform] that uses method channels.
class MethodChannelDexCompat extends DexCompatPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dex_compat');

  @override
  Future<bool> isDesktopMode() async {
    final isDesktop = await methodChannel.invokeMethod<bool>('isDesktopMode');
    return isDesktop ?? false;
  }
}
