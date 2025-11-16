# CLAUDE-zh.md

> **重要提示**: 本文件是 `CLAUDE.md` 的中文翻译版本。更新本文件时，必须同步更新 `CLAUDE.md` 以保持两个文件的内容一致。

本文件为 Claude Code (claude.ai/code) 在处理本仓库代码时提供指导。

## 项目概述

这是一个**多平台移动开发学习项目** (MulPlatformApplication)，在一个单一仓库中演示和比较不同的移动开发技术栈：

1. **android-app/**: Android 原生开发 + Jetpack Compose (Kotlin)
2. **flutter-app/**: 独立的 Flutter 应用
3. **flutter_module/**: 用于 Add-to-App 集成的 Flutter 模块，与 Android 原生应用集成
4. **react-native-app/**: 计划中的 React Native 项目（尚未实现）

项目目标是在不同技术栈中实现等效功能，以评估它们在生产部署中的优缺点。

## 架构设计

### Android 原生 + Flutter 模块集成

项目使用 Flutter 的 **Add-to-App** 模式，Android 原生应用 (`android-app/`) 将 Flutter 模块 (`flutter_module/`) 作为依赖嵌入：

- **android-app/settings.gradle.kts**: 将 `flutter_module/.android/Flutter` 引用为 `:flutter` 项目依赖
- **android-app/app/build.gradle.kts**: 实现 `implementation(project(":flutter"))` 依赖
- **MainActivity.kt**: 通过 `FlutterActivity.createDefaultIntent()` 启动 Flutter
- **AndroidManifest.xml**: 注册 `io.flutter.embedding.android.FlutterActivity`

Flutter 模块无法独立运行 - 它需要 Android 宿主应用。开发时使用 `flutter attach` 进行热重载。

### 项目结构

```
MulPlatformApplication/
├── android-app/              # Android 原生 (Jetpack Compose)
│   ├── app/src/main/
│   │   ├── java/com/vam/mulapp/
│   │   │   ├── MainActivity.kt       # 入口点，包含 Flutter 集成
│   │   │   └── ui/theme/            # Compose 主题文件
│   │   └── AndroidManifest.xml      # 在此注册 FlutterActivity
│   ├── build.gradle.kts
│   └── settings.gradle.kts          # 在此配置 Flutter 模块依赖
│
├── flutter-app/              # 独立的 Flutter 应用
│   ├── lib/
│   │   └── main.dart
│   ├── android/, ios/, web/, 等
│   └── pubspec.yaml
│
├── flutter_module/           # 用于嵌入的 Flutter 模块
│   ├── .android/             # 隐藏的 Android 集成配置
│   ├── .ios/                 # 隐藏的 iOS 集成配置
│   ├── lib/
│   │   ├── main.dart
│   │   └── pages/            # 多个 Flutter 页面
│   └── pubspec.yaml          # 包含模块特定配置
│
└── INTEGRATION_GUIDE.md      # 详细设置说明
```

## 常用开发命令

### Android 原生应用

```bash
cd android-app

# 构建应用
./gradlew assembleDebug

# 安装到已连接的设备/模拟器
./gradlew installDebug

# 清理构建
./gradlew clean

# 运行测试
./gradlew test
./gradlew connectedAndroidTest
```

**注意**: 构建 android-app 会自动编译 Flutter 模块作为依赖。

### Flutter 独立应用

```bash
cd flutter-app

# 获取依赖
flutter pub get

# 在已连接的设备上运行
flutter run

# 在特定设备上运行
flutter run -d <device-id>

# 构建 APK
flutter build apk

# 构建特定平台
flutter build ios
flutter build web

# 查看可用设备
flutter devices
```

### Flutter 模块开发

Flutter 模块嵌入在 Android 应用中。开发流程：

```bash
# 1. 安装并启动 Android 应用
cd android-app
./gradlew installDebug
# 打开应用并导航到 Flutter 页面

# 2. 在新终端中，附加 Flutter 调试器
cd flutter_module
flutter attach -d <device-id>

# 3. 修改 flutter_module/lib/*.dart 文件
# 4. 按 'r' 进行热重载，或按 'R' 进行热重启
# 5. 按 'q' 退出调试器
```

**重要**: Flutter 模块无法使用 `flutter run` 独立运行。必须从 Android 宿主应用启动。

### 环境验证

```bash
# 检查 Flutter 安装
flutter doctor

# 检查 Java 版本（应为 11+）
java -version

# 接受 Android 许可
flutter doctor --android-licenses
```

## 关键配置文件

### Android 原生集成

- **android-app/settings.gradle.kts**:
  - 设置 `flutterProjectRoot` 为 `../flutter_module`
  - 从 `flutter_module/.android/Flutter` 包含 `:flutter` 项目
  - 应用 Flutter SDK 的 `module_plugin_loader.gradle`

- **android-app/app/build.gradle.kts**:
  - 包名: `com.vam.mulapp`
  - 最小 SDK: 24 (Android 7.0)
  - 目标 SDK: 36
  - 依赖于 `project(":flutter")`

### Flutter 模块配置

- **flutter_module/pubspec.yaml**:
  - 模块标识符: `flutter_module`
  - AndroidX: 已启用
  - Android 包名: `com.vam.mulapp.flutter_module`
  - iOS bundle: `com.vam.mulapp.flutterModule`

- **flutter_module/.android/local.properties**:
  - 包含 `flutter.sdk` 路径（自动生成）
  - Gradle 需要此文件来定位 Flutter SDK

## 技术栈详情

### Android 原生 (android-app)
- **语言**: Kotlin
- **UI**: Jetpack Compose + Material3
- **构建**: Gradle 8.5+ 使用 Kotlin DSL
- **JDK**: 11（兼容 17）
- **最小 SDK**: API 24 (Android 7.0)

### Flutter 项目
- **Flutter SDK**: 3.16.3+
- **Dart SDK**: 3.2.3+
- **支持平台** (flutter-app): Android, iOS, Web, macOS, Windows, Linux
- **支持平台** (flutter_module): 仅 Android, iOS（Add-to-App 限制）

## 开发工作流

### 典型开发会话

1. **仅 Android 更改**: 编辑 `android-app/` 中的 Kotlin 文件，从 Android Studio 运行
2. **仅 Flutter 更改**: 编辑 `flutter_module/` 中的 Dart 文件，使用 `flutter attach` 工作流
3. **跨平台更改**:
   - 进行 Android 更改，使用 Gradle 重新构建
   - 进行 Flutter 更改，使用 `r` 键热重载
   - 彻底测试集成

### 测试集成

修改 Flutter 模块后：
1. 重新构建 Android 应用: `cd android-app && ./gradlew installDebug`
2. 启动应用并导航到 Flutter 页面
3. 附加调试器: `cd flutter_module && flutter attach`
4. 验证更改是否正确显示

## 常见问题

### 找不到 Flutter 模块

**错误**: `Could not find :flutter`

**解决方案**:
1. 验证 `flutter_module/.android/local.properties` 存在且包含正确的 `flutter.sdk` 路径
2. 运行 `cd flutter_module && flutter pub get` 重新生成隐藏的 Android 配置
3. 检查 `android-app/settings.gradle.kts` 是否有正确的 `flutterProjectRoot` 路径

### Gradle 版本不兼容

**错误**: `Unsupported class file major version 65`

**解决方案**: 更新 `flutter_module/.android/gradle/wrapper/gradle-wrapper.properties` 中的 Gradle 包装器：
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-all.zip
```

### Flutter Attach 失败

**成功附加的要求**:
1. Flutter 页面必须在屏幕上可见
2. 设备/模拟器必须通过 ADB 连接
3. 从 `flutter_module/` 目录运行（不是根目录）
4. 使用 `flutter devices` 中的正确设备 ID

## 学习路线图

当前阶段: **阶段 1 - Flutter 基础** ✅

计划阶段：
1. Flutter 基础和 API 集成（当前）
2. Android Compose 等效实现
3. React Native 实现
4. 技术比较和生产部署

用于比较的目标功能：
- 用户认证
- 数据列表和详情视图
- 搜索功能
- 本地缓存和离线支持
- 主题切换（深色/浅色模式）
- 多语言支持
