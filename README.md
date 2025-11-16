# MulPlatformApplication - 多平台移动开发学习项目

## 项目简介

这是一个用于学习和比较不同移动开发技术栈的项目。通过在同一个 Monorepo 中实践 Android Native/Compose、Flutter 和 React Native，深入理解各技术栈的特点和差异。

最终目标是实现一个调用公共平台 API 的移动应用，选择最适合的技术栈进行生产部署。

## 项目结构

```
MulPlatformApplication/
├── android-app/          # Android Native + Jetpack Compose 项目
├── flutter-app/          # Flutter 项目
├── react-native-app/     # React Native 项目 (计划中)
└── README.md            # 本文件
```

## 技术栈

### 1. Android Native + Jetpack Compose
- **位置**: `android-app/`
- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **构建工具**: Gradle

### 2. Flutter
- **位置**: `flutter-app/`
- **语言**: Dart
- **UI框架**: Flutter Widgets
- **特点**: 跨平台 (iOS/Android/Web/Desktop)

### 3. React Native (计划中)
- **位置**: `react-native-app/`
- **语言**: JavaScript/TypeScript
- **UI框架**: React Native
- **特点**: 使用 JavaScript 开发原生应用

## 快速开始

### Android 项目

```bash
cd android-app
./gradlew build
./gradlew installDebug
```

在 Android Studio 中打开 `android-app` 目录即可开始开发。

### Flutter 项目

```bash
cd flutter-app

# 获取依赖
flutter pub get

# 运行应用 (需要连接设备或启动模拟器)
flutter run

# 查看支持的平台
flutter devices
```

**支持的平台**:
- Android
- iOS (需要 macOS)
- Web
- macOS Desktop
- Windows Desktop
- Linux Desktop

### React Native 项目 (即将添加)

敬请期待...

## 开发环境要求

### Android 开发
- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 11 或更高版本
- Android SDK 34

### Flutter 开发
- Flutter SDK 3.16.3 或更高版本
- Dart SDK 3.2.3 或更高版本
- 对应平台的开发工具:
  - Android: Android Studio
  - iOS: Xcode (仅 macOS)
  - Web: Chrome
  - Desktop: 对应平台的构建工具

### React Native 开发 (计划中)
- Node.js 18+
- npm 或 yarn
- React Native CLI

## 学习路线

1. **Phase 1**: Flutter 基础学习与实践 ✅ (当前阶段)
   - 学习 Flutter 基础组件和布局
   - 实现状态管理
   - 调用 REST API
   - 构建完整的功能模块

2. **Phase 2**: Android Compose 开发
   - 学习 Jetpack Compose UI
   - 实现相同功能进行对比
   - 深入理解 Android 生态

3. **Phase 3**: React Native 实践
   - 创建 React Native 项目
   - 实现相同功能
   - 技术栈横向比较

4. **Phase 4**: 技术选型与最终实现
   - 对比各技术栈的优缺点
   - 选择最适合的技术栈
   - 实现完整的生产级应用

## 应用功能规划

基于公共 API 的移动应用功能:
- [ ] 用户认证与授权
- [ ] 数据展示列表
- [ ] 详情页面
- [ ] 搜索功能
- [ ] 本地数据缓存
- [ ] 离线支持
- [ ] 主题切换 (深色/浅色模式)
- [ ] 多语言支持

## API 资源

待选择的公共 API:
- [GitHub API](https://docs.github.com/en/rest)
- [JSONPlaceholder](https://jsonplaceholder.typicode.com/) (测试用)
- [OpenWeatherMap API](https://openweathermap.org/api)
- [NewsAPI](https://newsapi.org/)
- 其他...

## 版本控制说明

本项目使用 Git 进行版本控制。各子项目保持独立的 `.gitignore` 配置。

## 许可证

本项目仅用于学习目的。

## 联系方式

如有问题或建议，欢迎提 Issue。
