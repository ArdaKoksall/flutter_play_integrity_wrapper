# flutter_play_integrity_wrapper

A Flutter plugin wrapper for the Google Play Integrity API on Android.

## Features

- Request an integrity token from the Google Play Integrity API.
- Supports providing a custom nonce or generating a secure random one.

## Getting Started

1.  Add `flutter_play_integrity_wrapper` to your `pubspec.yaml` dependencies.
2.  Enable the Play Integrity API in your Google Cloud Console project.
3.  Link your Google Cloud project to your app in the Google Play Console.

## Usage

Import the package:

```dart
import 'package:flutter_play_integrity_wrapper/flutter_play_integrity_wrapper.dart';
```

Create an instance of `FlutterPlayIntegrityWrapper` and call `requestIntegrityToken`:

```dart
final _playIntegrityWrapper = FlutterPlayIntegrityWrapper();

try {
  // Use your actual Cloud Project Number
  final String? token = await _playIntegrityWrapper.requestIntegrityToken(
    cloudProjectNumber: '1234567890', 
    // optional: nonce: 'your-custom-nonce' 
  );
  
  if (token != null) {
    print('Integrity Token: $token');
    // Send this token to your backend for verification
  }
} catch (e) {
  print('Error requesting integrity token: $e');
}
```

## Security Note

The `nonce` parameter is optional. If not provided, a secure random nonce is generated automatically. However, for better security against replay attacks, it is highly recommended to generate the nonce on your backend server and pass it to the app.

## Android Configuration

Ensure your `minSdkVersion` in `android/app/build.gradle` is at least 19 (Google Play Services requirement).

This plugin requires Google Play Services to be available on the device.

