package com.vam.mulapp

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * 自定义 Application 类
 * 用于在应用启动时预热 Flutter 引擎
 */
class MyApplication : Application() {

    companion object {
        // Flutter 引擎缓存 ID
        const val FLUTTER_ENGINE_ID = "main_flutter_engine"
    }

    override fun onCreate() {
        super.onCreate()

        // 预热 Flutter 引擎
        warmUpFlutterEngine()
    }

    /**
     * 预热 Flutter 引擎
     * 在应用启动时初始化 Flutter 引擎并缓存
     */
    private fun warmUpFlutterEngine() {
        // 创建 FlutterEngine 实例
        val flutterEngine = FlutterEngine(this)

        // 启动 Dart 执行器，执行 Flutter 应用的入口
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // 将引擎缓存起来，供后续使用
        FlutterEngineCache
            .getInstance()
            .put(FLUTTER_ENGINE_ID, flutterEngine)

        // 日志输出（可选）
        android.util.Log.d("MyApplication", "Flutter engine warmed up successfully")
    }
}
