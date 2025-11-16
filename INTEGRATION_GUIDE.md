# Android 原生应用集成 Flutter 模块完整指南

本文档详细说明如何创建一个 Android 原生应用，并集成 Flutter 模块实现混合开发。

## 📋 目录

- [环境要求](#环境要求)
- [步骤 1: 创建 Android 原生应用](#步骤-1-创建-android-原生应用)
- [步骤 2: 创建 Flutter 模块](#步骤-2-创建-flutter-模块)
- [步骤 3: 配置 Android 项目集成 Flutter](#步骤-3-配置-android-项目集成-flutter)
- [步骤 4: 在 Android 中添加启动 Flutter 的入口](#步骤-4-在-android-中添加启动-flutter-的入口)
- [步骤 5: 注册 FlutterActivity](#步骤-5-注册-flutteractivity)
- [步骤 6: 构建和运行](#步骤-6-构建和运行)
- [步骤 7: 开发时使用热重载](#步骤-7-开发时使用热重载)
- [常见问题](#常见问题)

---

## 环境要求

### 必需工具

- **Android Studio**: Hedgehog (2023.1.1) 或更高版本
- **Flutter SDK**: 3.0.0 或更高版本
- **Dart SDK**: 通过 Flutter SDK 安装
- **JDK**: 11 或更高版本（推荐 17）
- **Gradle**: 8.0+ （通过项目配置）

### 验证环境

```bash
# 检查 Flutter 安装
flutter doctor

# 检查 Java 版本
java -version

# 检查 Android SDK
flutter doctor --android-licenses
```

---

## 步骤 1: 创建 Android 原生应用

### 1.1 使用 Android Studio 创建项目

1. 打开 Android Studio
2. 选择 **File** → **New** → **New Project**
3. 选择 **Empty Activity**
4. 配置项目：
   - **Name**: `MulPlatformApplication`
   - **Package name**: `com.vam.mulapp`
   - **Save location**: 选择你的项目目录
   - **Language**: Kotlin
   - **Minimum SDK**: API 24 (Android 7.0)
   - **Build configuration language**: Kotlin DSL

5. 点击 **Finish**

### 1.2 项目结构

创建完成后，你会得到以下结构：

```
android-app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/vam/mulapp/
│   │       │   └── MainActivity.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

### 1.3 修改 MainActivity 使用 Compose

编辑 `app/src/main/java/com/vam/mulapp/MainActivity.kt`:

```kotlin
package com.vam.mulapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vam.mulapp.ui.theme.MulPlatformApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MulPlatformApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenFlutter = {
                            // 稍后添加启动 Flutter 的代码
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onOpenFlutter: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello Android Native!",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onOpenFlutter) {
            Text(text = "打开 Flutter 页面")
        }
    }
}
```

---

## 步骤 2: 创建 Flutter 模块

### 2.1 在项目根目录创建 Flutter 模块

```bash
# 进入项目根目录
cd /path/to/your/project

# 创建 Flutter 模块
flutter create --template module --org com.vam.mulapp flutter_module
```

**参数说明：**
- `--template module`: 指定创建 Flutter 模块（而非独立应用）
- `--org com.vam.mulapp`: 组织名，需与 Android 项目包名一致
- `flutter_module`: 模块名称

### 2.2 验证 Flutter 模块创建成功

```bash
cd flutter_module
ls -la
```

你应该看到：

```
flutter_module/
├── .android/          # Android 集成配置（隐藏目录）
├── .ios/              # iOS 集成配置（隐藏目录）
├── lib/
│   └── main.dart      # Flutter 代码
├── pubspec.yaml       # 依赖配置
└── test/
```

### 2.3 获取 Flutter 依赖

```bash
cd flutter_module
flutter pub get
```

### 2.4 自定义 Flutter 页面（可选）

编辑 `flutter_module/lib/main.dart`，修改页面文字：

```dart
import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter 页面'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              '欢迎来到 Flutter 页面！',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            const Text(
              '你已经点击了这么多次:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
```

---

## 步骤 3: 配置 Android 项目集成 Flutter

### 3.1 修改 `settings.gradle.kts`

编辑 `android-app/settings.gradle.kts`，添加 Flutter 模块引用：

```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // 添加 Flutter 仓库
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
    }
}

rootProject.name = "MulPlatformApplication"
include(":app")

// ========== Flutter 模块集成 ==========
val flutterProjectRoot = settingsDir.parentFile.resolve("flutter_module")

// 包含 Flutter 模块
include(":flutter")
project(":flutter").projectDir = flutterProjectRoot.resolve(".android/Flutter")

// 加载 Flutter SDK 路径
val localPropertiesFile = flutterProjectRoot.resolve(".android/local.properties")
val properties = java.util.Properties()
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
val flutterSdkPath = properties.getProperty("flutter.sdk")

// 应用 Flutter 插件加载器
if (flutterSdkPath != null) {
    val flutterPluginLoader = File(flutterSdkPath)
        .resolve("packages/flutter_tools/gradle/module_plugin_loader.gradle")
    apply(from = flutterPluginLoader)
}
```

### 3.2 修改 `app/build.gradle.kts`

编辑 `android-app/app/build.gradle.kts`，添加 Flutter 依赖：

```kotlin
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // ========== Flutter 模块依赖 ==========
    implementation(project(":flutter"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

---

## 步骤 4: 在 Android 中添加启动 Flutter 的入口

### 4.1 导入 Flutter 相关类

修改 `MainActivity.kt`，添加 Flutter 导入：

```kotlin
package com.vam.mulapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vam.mulapp.ui.theme.MulPlatformApplicationTheme
// ========== 导入 Flutter ==========
import io.flutter.embedding.android.FlutterActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MulPlatformApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenFlutter = {
                            // ========== 启动 Flutter Activity ==========
                            startActivity(
                                FlutterActivity.createDefaultIntent(this)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onOpenFlutter: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello Android Native!",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onOpenFlutter) {
            Text(text = "打开 Flutter 页面")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MulPlatformApplicationTheme {
        MainScreen(onOpenFlutter = {})
    }
}
```

---

## 步骤 5: 注册 FlutterActivity

### 5.1 修改 AndroidManifest.xml

编辑 `android-app/app/src/main/AndroidManifest.xml`：

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MulPlatformApplication">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MulPlatformApplication">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- ========== Flutter Activity 注册 ========== -->
        <activity
            android:name="io.flutter.embedding.android.FlutterActivity"
            android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
            android:hardwareAccelerated="true"
            android:windowSoftInputMode="adjustResize" />
    </application>

</manifest>
```

**配置说明：**
- `android:configChanges`: 处理配置变化（如屏幕旋转），避免 Activity 重新创建
- `android:hardwareAccelerated="true"`: 启用硬件加速，提升 Flutter 渲染性能
- `android:windowSoftInputMode="adjustResize"`: 键盘弹出时调整窗口大小

---

## 步骤 6: 构建和运行

### 6.1 清理项目

```bash
cd android-app
./gradlew clean
```

### 6.2 构建项目

```bash
./gradlew assembleDebug
```

**注意事项：**
- 首次构建会下载 Flutter 引擎，可能需要几分钟
- 确保网络连接正常

### 6.3 安装到设备

```bash
./gradlew installDebug
```

或在 Android Studio 中点击运行按钮。

### 6.4 测试功能

1. 启动应用
2. 点击"打开 Flutter 页面"按钮
3. 应该能看到 Flutter 页面显示
4. 点击 + 按钮，计数器应该增加

---

## 步骤 7: 开发时使用热重载

### 7.1 启动应用并进入 Flutter 页面

```bash
# 方式 1: 从 Android Studio 运行
# 点击 Run 按钮，然后在应用中点击"打开 Flutter 页面"

# 方式 2: 命令行安装
cd android-app
./gradlew installDebug
# 手动打开应用，点击"打开 Flutter 页面"
```

### 7.2 连接 Flutter 调试器

在新终端中：

```bash
cd flutter_module
flutter attach -d <设备ID>

# 查看设备 ID
flutter devices

# 示例
flutter attach -d 3f4733f0
```

### 7.3 看到连接成功提示

```
✓  Built build/app/outputs/flutter-apk/app.apk.
Syncing files to device KB2000...
Flutter run key commands.
r Hot reload. 🔥
R Hot restart.
h List all available interactive commands.
d Detach (terminate "flutter run" but leave application running).
c Clear the screen
q Quit (terminate the application on the device).
```

### 7.4 开始热重载开发

1. 修改 `flutter_module/lib/main.dart`
2. 保存文件
3. 在终端按 **r** 键
4. 手机上立即看到变化！

**常用快捷键：**
- **r** - 热重载（Hot Reload）- 保留状态，快速刷新
- **R** - 热重启（Hot Restart）- 重置状态，重新启动
- **p** - 显示调试边界和网格
- **q** - 退出调试

---

## 常见问题

### 问题 1: Gradle 版本不兼容

**错误信息：**
```
Unsupported class file major version 65
```

**解决方案：**

升级 Gradle 版本。编辑 `flutter_module/.android/gradle/wrapper/gradle-wrapper.properties`:

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-all.zip
```

---

### 问题 2: 找不到 FlutterActivity

**错误信息：**
```
android.content.ActivityNotFoundException: Unable to find explicit activity class
{com.vam.mulapp/io.flutter.embedding.android.FlutterActivity}
```

**解决方案：**

确保在 `AndroidManifest.xml` 中注册了 FlutterActivity（参见步骤 5）。

---

### 问题 3: Flutter 模块未找到

**错误信息：**
```
Could not find :flutter
```

**解决方案：**

1. 检查 `settings.gradle.kts` 中的 Flutter 模块路径是否正确
2. 确保 `flutter_module/.android/local.properties` 存在且包含 `flutter.sdk` 配置
3. 运行 `flutter pub get` 确保依赖已获取

---

### 问题 4: 安装 APK 时只有"取消安装"选项

**原因：**
包名冲突或签名不匹配

**解决方案：**

1. 手动卸载设备上的旧版本应用
2. 或修改 `build.gradle.kts` 中的 `applicationId` 为唯一值

---

### 问题 5: flutter attach 无法连接

**解决方案：**

1. 确保 Flutter 页面在前台显示
2. 确保设备已通过 USB 连接并授权调试
3. 尝试重新启动应用
4. 检查设备 ID 是否正确：`flutter devices`

---

## 项目结构总结

最终项目结构：

```
MulPlatformApplication/
│
├── android-app/              # Android 原生应用
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/vam/mulapp/
│   │   │   │   └── MainActivity.kt         # 包含启动 Flutter 的按钮
│   │   │   └── AndroidManifest.xml         # 注册了 FlutterActivity
│   │   └── build.gradle.kts                # 依赖 :flutter
│   └── settings.gradle.kts                 # 引用 flutter_module
│
└── flutter_module/           # Flutter 模块
    ├── .android/             # Android 集成配置
    ├── lib/
    │   └── main.dart         # Flutter 代码
    └── pubspec.yaml          # Flutter 依赖
```

---

## 开发工作流

### 日常开发流程

```bash
# 1. 启动 Android 应用
cd android-app
./gradlew installDebug

# 2. 手机上点击"打开 Flutter 页面"

# 3. 连接 Flutter 调试器
cd flutter_module
flutter attach -d <device-id>

# 4. 修改 Flutter 代码 → 按 r → 立即看到效果
# 无需重新编译整个 Android 应用！
```

---

## 技术要点总结

### Flutter Module vs Flutter App

| 特性 | Flutter Module | Flutter App |
|------|----------------|-------------|
| 创建命令 | `flutter create --template module` | `flutter create` |
| 用途 | 集成到现有原生应用 | 独立运行的 Flutter 应用 |
| 目录结构 | `.android/`, `.ios/`（隐藏） | `android/`, `ios/`（可见） |
| 独立运行 | ❌ 需要宿主应用 | ✅ 可以 |
| 平台支持 | Android, iOS | Android, iOS, Web, Desktop |

### Add-to-App 模式优势

1. **渐进式迁移**：可以逐步将原生应用的部分功能迁移到 Flutter
2. **复用现有代码**：保留原生代码和业务逻辑
3. **团队协作**：原生和 Flutter 团队可并行开发
4. **降低风险**：不需要完全重写应用

---

## 扩展学习

### 下一步可以学习：

1. **Platform Channel**：实现 Android 与 Flutter 双向通信
2. **传递参数到 Flutter**：启动 Flutter 时传递初始数据
3. **Flutter 调用 Android 原生方法**：如相机、GPS 等
4. **优化性能**：减小 APK 体积、优化首次加载速度
5. **多个 Flutter 页面**：在应用中集成多个 Flutter 页面

### 参考资源

- [Flutter 官方文档 - Add-to-App](https://docs.flutter.dev/add-to-app)
- [Platform Channel 文档](https://docs.flutter.dev/platform-integration/platform-channels)
- [Flutter 性能优化](https://docs.flutter.dev/perf)

---

## 版本信息

本指南基于以下版本测试：

- Flutter: 3.16.3
- Dart: 3.2.3
- Gradle: 8.5
- Android Gradle Plugin: 8.1.0
- Kotlin: 1.9+

---

**文档创建日期**: 2025-01-16

**作者**: Claude Code

**许可**: MIT License

---

如有问题或建议，欢迎提 Issue！
