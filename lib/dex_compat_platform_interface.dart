import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'dex_compat_method_channel.dart';

abstract class DexCompatPlatform extends PlatformInterface {
  /// Constructs a DexCompatPlatform.
  DexCompatPlatform() : super(token: _token);

  static final Object _token = Object();

  static DexCompatPlatform _instance = MethodChannelDexCompat();

  /// The default instance of [DexCompatPlatform] to use.
  ///
  /// Defaults to [MethodChannelDexCompat].
  static DexCompatPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DexCompatPlatform] when
  /// they register themselves.
  static set instance(DexCompatPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  /// Returns `true` if the app is running in a desktop windowing environment
  /// such as Samsung DeX.
  Future<bool> isDesktopMode() {
    throw UnimplementedError('isDesktopMode() has not been implemented.');
  }
}
