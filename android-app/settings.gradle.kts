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
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
    }
}

rootProject.name = "MulPlatformApplication"
include(":app")

// Flutter module integration
val flutterProjectRoot = settingsDir.parentFile.resolve("flutter_module")

// Include Flutter module
include(":flutter")
project(":flutter").projectDir = flutterProjectRoot.resolve(".android/Flutter")

// Load Flutter SDK path from local.properties
val localPropertiesFile = flutterProjectRoot.resolve(".android/local.properties")
val properties = java.util.Properties()
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
val flutterSdkPath = properties.getProperty("flutter.sdk")

// Apply Flutter plugin loader
if (flutterSdkPath != null) {
    val flutterPluginLoader = File(flutterSdkPath).resolve("packages/flutter_tools/gradle/module_plugin_loader.gradle")
    apply(from = flutterPluginLoader)
}
