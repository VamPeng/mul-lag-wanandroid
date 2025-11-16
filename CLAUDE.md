# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **multi-platform mobile development learning project** (MulPlatformApplication) that demonstrates and compares different mobile development stacks in a monorepo:

1. **android-app/**: Android Native with Jetpack Compose (Kotlin)
2. **flutter-app/**: Standalone Flutter application
3. **flutter_module/**: Flutter module for Add-to-App integration with Android native app
4. **react-native-app/**: Planned React Native project (not yet implemented)

The goal is to implement equivalent features across different technology stacks to evaluate their strengths and weaknesses for production deployment.

## Architecture

### Android Native + Flutter Module Integration

The project uses Flutter's **Add-to-App** pattern where the Android native app (`android-app/`) embeds the Flutter module (`flutter_module/`) as a dependency:

- **android-app/settings.gradle.kts**: References `flutter_module/.android/Flutter` as `:flutter` project dependency
- **android-app/app/build.gradle.kts**: Implements `implementation(project(":flutter"))` dependency
- **MainActivity.kt**: Launches Flutter via `FlutterActivity.createDefaultIntent()`
- **AndroidManifest.xml**: Registers `io.flutter.embedding.android.FlutterActivity`

The Flutter module cannot run standalone - it requires the Android host app. Use `flutter attach` for hot reload during development.

### Project Structure

```
MulPlatformApplication/
├── android-app/              # Android Native (Jetpack Compose)
│   ├── app/src/main/
│   │   ├── java/com/vam/mulapp/
│   │   │   ├── MainActivity.kt       # Entry point with Flutter integration
│   │   │   └── ui/theme/            # Compose theme files
│   │   └── AndroidManifest.xml      # FlutterActivity registered here
│   ├── build.gradle.kts
│   └── settings.gradle.kts          # Flutter module dependency configured here
│
├── flutter-app/              # Standalone Flutter app
│   ├── lib/
│   │   └── main.dart
│   ├── android/, ios/, web/, etc.
│   └── pubspec.yaml
│
├── flutter_module/           # Flutter module for embedding
│   ├── .android/             # Hidden Android integration config
│   ├── .ios/                 # Hidden iOS integration config
│   ├── lib/
│   │   ├── main.dart
│   │   └── pages/            # Multiple Flutter pages
│   └── pubspec.yaml          # Contains module-specific config
│
└── INTEGRATION_GUIDE.md      # Detailed setup instructions
```

## Common Development Commands

### Android Native App

```bash
cd android-app

# Build the app
./gradlew assembleDebug

# Install to connected device/emulator
./gradlew installDebug

# Clean build
./gradlew clean

# Run tests
./gradlew test
./gradlew connectedAndroidTest
```

**Note**: Building android-app will automatically compile the Flutter module as a dependency.

### Flutter Standalone App

```bash
cd flutter-app

# Get dependencies
flutter pub get

# Run on connected device
flutter run

# Run on specific device
flutter run -d <device-id>

# Build APK
flutter build apk

# Build for specific platforms
flutter build ios
flutter build web

# Check available devices
flutter devices
```

### Flutter Module Development

The Flutter module is embedded in the Android app. Development workflow:

```bash
# 1. Install and launch the Android app
cd android-app
./gradlew installDebug
# Open app and navigate to Flutter page

# 2. In a new terminal, attach Flutter debugger
cd flutter_module
flutter attach -d <device-id>

# 3. Make changes to flutter_module/lib/*.dart
# 4. Press 'r' for hot reload or 'R' for hot restart
# 5. Press 'q' to quit debugger
```

**Important**: The Flutter module CANNOT run standalone with `flutter run`. It must be launched from the Android host app.

### Environment Verification

```bash
# Check Flutter installation
flutter doctor

# Check Java version (should be 11+)
java -version

# Accept Android licenses
flutter doctor --android-licenses
```

## Key Configuration Files

### Android Native Integration

- **android-app/settings.gradle.kts**:
  - Sets `flutterProjectRoot` to `../flutter_module`
  - Includes `:flutter` project from `flutter_module/.android/Flutter`
  - Applies Flutter SDK's `module_plugin_loader.gradle`

- **android-app/app/build.gradle.kts**:
  - Package: `com.vam.mulapp`
  - Min SDK: 24 (Android 7.0)
  - Target SDK: 36
  - Depends on `project(":flutter")`

### Flutter Module Configuration

- **flutter_module/pubspec.yaml**:
  - Module identifier: `flutter_module`
  - AndroidX: enabled
  - Android package: `com.vam.mulapp.flutter_module`
  - iOS bundle: `com.vam.mulapp.flutterModule`

- **flutter_module/.android/local.properties**:
  - Contains `flutter.sdk` path (auto-generated)
  - Required for Gradle to locate Flutter SDK

## Technology Stack Details

### Android Native (android-app)
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Build**: Gradle 8.5+ with Kotlin DSL
- **JDK**: 11 (compatible with 17)
- **Minimum SDK**: API 24 (Android 7.0)

### Flutter Projects
- **Flutter SDK**: 3.16.3+
- **Dart SDK**: 3.2.3+
- **Supported platforms** (flutter-app): Android, iOS, Web, macOS, Windows, Linux
- **Supported platforms** (flutter_module): Android, iOS only (Add-to-App limitation)

## Development Workflow

### Typical Development Session

1. **Android-only changes**: Edit Kotlin files in `android-app/`, run from Android Studio
2. **Flutter-only changes**: Edit Dart files in `flutter_module/`, use `flutter attach` workflow
3. **Cross-platform changes**:
   - Make Android changes, rebuild with Gradle
   - Make Flutter changes, hot reload with `r` key
   - Test integration thoroughly

### Testing Integration

After making changes to Flutter module:
1. Rebuild Android app: `cd android-app && ./gradlew installDebug`
2. Launch app and navigate to Flutter screen
3. Attach debugger: `cd flutter_module && flutter attach`
4. Verify changes appear correctly

## Common Issues

### Flutter Module Not Found

**Error**: `Could not find :flutter`

**Solution**:
1. Verify `flutter_module/.android/local.properties` exists with correct `flutter.sdk` path
2. Run `cd flutter_module && flutter pub get` to regenerate hidden Android config
3. Check `android-app/settings.gradle.kts` has correct `flutterProjectRoot` path

### Gradle Version Incompatibility

**Error**: `Unsupported class file major version 65`

**Solution**: Update Gradle wrapper in `flutter_module/.android/gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-all.zip
```

### Flutter Attach Fails

**Requirements for successful attach**:
1. Flutter page must be visible on screen
2. Device/emulator must be connected via ADB
3. Run from `flutter_module/` directory (not root)
4. Use correct device ID from `flutter devices`

## Learning Roadmap

Current Phase: **Phase 1 - Flutter Foundation** ✅

Planned phases:
1. Flutter basics and API integration (current)
2. Android Compose equivalent implementation
3. React Native implementation
4. Technology comparison and production deployment

Target features for comparison:
- User authentication
- Data listing and detail views
- Search functionality
- Local caching and offline support
- Theme switching (dark/light mode)
- Multi-language support
