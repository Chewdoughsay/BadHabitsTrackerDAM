plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android) // Plugin Hilt
    alias(libs.plugins.ksp)          // Plugin KSP (înlocuitor KAPT)
    alias(libs.plugins.kotlin.serialization) // Pentru navigare cu argumente type-safe
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.badhabitstrackerdam" // Schimbă cu pachetul tău real
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.badhabitstrackerdam"
        minSdk = 26 // Android 8.0 (recomandat pentru Room și funcții moderne)
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Doar dacă folosești versiuni vechi de Kotlin, la 2.0.0 nu mai e nevoie
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core Android & Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Hilt - Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // Folosim KSP
    implementation(libs.androidx.hilt.navigation.compose) // Integrare Hilt cu Nav Compose

    // Room - Local Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Suport Coroutines/Flow
    ksp(libs.androidx.room.compiler) // Folosim KSP

    // Retrofit - Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}