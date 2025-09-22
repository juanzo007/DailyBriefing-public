plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.dailybriefing"
    compileSdk = 35   // Android 15

    defaultConfig {
        applicationId = "com.dailybriefing"
        minSdk = 26
        targetSdk = 35  // Android 15
        versionCode = 1
        versionName = "0.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.material3:material3:1.3.0")
    // ...your other deps
}
