import 'dart:convert';
import 'dart:math';
import 'package:flutter/services.dart';

class FlutterPlayIntegrityWrapper {
  // Must match the channel name in Kotlin
  static const MethodChannel _channel = MethodChannel('flutter_play_integrity_wrapper');

  /// Requests an integrity token from Google Play.
  ///
  /// [cloudProjectNumber]: The numeric project ID from Google Cloud Console.
  /// [nonce]: A unique string. If null, a secure random nonce (32 bytes) is generated automatically.
  ///
  /// **Security Note:** It is highly recommended to generate the [nonce] on your
  /// backend server to prevent replay attacks. Only use the auto-generated nonce
  /// for simple checks where replay attacks are not a concern.
  ///
  /// Returns the encrypted token string, or throws a [PlatformException] on failure.
  Future<String?> requestIntegrityToken({
    required String cloudProjectNumber,
    String? nonce,
  }) async {
    // 1. Use provided nonce OR generate a secure one if null
    final String finalNonce = nonce ?? _generateSecureNonce();

    // 2. Call the native platform
    final String? token = await _channel.invokeMethod('requestIntegrityToken', {
      'nonce': finalNonce,
      'cloudProjectNumber': cloudProjectNumber,
    });

    return token;
  }

  /// Generates a cryptographically secure random nonce.
  ///
  /// Google requires the raw bytes (before base64) to be > 16 bytes.
  /// We generate 32 bytes to be safe.
  String _generateSecureNonce() {
    final random = Random.secure();
    final values = List<int>.generate(32, (i) => random.nextInt(256));
    return base64Encode(values);
  }
}