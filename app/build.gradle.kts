plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.dailybriefing"

    // 👇 This removes the "compileSdkVersion is not specified" error
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dailybriefing"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    // Java toolchain alignment
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            // keep defaults
        }
    }
}

// Kotlin toolchain + new compilerOptions DSL (replaces deprecated kotlinOptions)
kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        // (optional) freeCompilerArgs.add("-Xjsr305=strict")
    }
}

dependencies {
    // Credential Manager
    implementation("androidx.credentials:credentials:1.6.0-alpha05")
    implementation("androidx.credentials:credentials-play-services-auth:1.6.0-alpha05")

    // Google ID helper
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // Play Services Auth
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // UI
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // WorkManager (if you’re using CoroutineWorker)
    implementation("androidx.work:work-runtime-ktx:2.9.1")
}
