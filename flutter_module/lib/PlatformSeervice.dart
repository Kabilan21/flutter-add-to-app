import 'package:flutter/services.dart';

class PlatformService {
  final eventChannel = const EventChannel('com.example.example/events');
  static const methodChannel =
      const MethodChannel('com.example.example/methods');

  Future<void> navigateToNative() async {
    try {
      return await methodChannel.invokeMethod("navigate_to_native");
    } on PlatformException catch (e) {
      print(e.message.toString());
      return 0;
    }
  }
}
