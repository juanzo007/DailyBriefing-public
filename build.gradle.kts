// ROOT build.gradle.kts — explicit plugin IDs (no version catalog)
plugins {
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    // keep compose plugin if/when you use Compose:
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false

}