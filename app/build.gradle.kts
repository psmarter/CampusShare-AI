plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.campus_item_sharing"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.campus_item_sharing"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // Retrofit and Gson for networking
    implementation(libs.retrofit) // Retrofit library
    implementation(libs.converter.gson) // Gson converter for Retrofit
    implementation(libs.gson) // Gson for JSON parsing

    // AndroidX Core and Lifecycle
    implementation(libs.androidx.core.ktx) // Core KTX
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle KTX

    // Compose UI and Material3
    implementation(libs.androidx.activity.compose) // Compose support for activities
    implementation(platform(libs.androidx.compose.bom)) // Compose BOM (Bill of Materials)
    implementation(libs.androidx.ui) // Compose UI
    implementation(libs.androidx.ui.graphics) // Compose Graphics
    implementation(libs.androidx.ui.tooling.preview) // Compose Preview
    implementation(libs.androidx.material3) // Material 3 Design System

    // ConstraintLayout and Material Design
    implementation(libs.androidx.constraintlayout) // ConstraintLayout
    implementation(libs.material)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.firebase.firestore.ktx)

    // Testing dependencies
    testImplementation(libs.junit) // Unit testing with JUnit
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Compose BOM for testing
    androidTestImplementation(libs.androidx.ui.test.junit4) // Compose UI testing with JUnit4
    debugImplementation(libs.androidx.ui.tooling) // Debugging tools for Compose
    debugImplementation(libs.androidx.ui.test.manifest) // Compose UI testing manifest

    // Optional: OkHttp for logging (if needed)
    implementation(libs.logging.interceptor) // Logging for debugging HTTP requests

    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler) // Glide Compiler

    implementation(libs.material)
    implementation(libs.generativeai)

    implementation(libs.mlkit.barcode)
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.zxing.android)
    implementation(libs.guava)

    implementation(libs.biweekly)
    //implementation(libs.weekview)
    //implementation(libs.weekview.core)
    implementation(libs.weekview.core)
}