import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
}

project.extensions.configure<LibraryExtension> {
    namespace = "com.example.simgplechatexample"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        compose = true
    }

    defaultConfig {
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.foundation)
    implementation(libs.material)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    ksp(libs.androidx.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)

    implementation(libs.coroutines)

    implementation(libs.androidx.paging.runtime.ktx)


    // Для Compose
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.activity.compose)

    platform(libs.compose.bom).apply {
        implementation(this)
        androidTestImplementation(this)
    }
    implementation(libs.androidx.paging.compose)
    implementation(libs.material3)
    implementation(libs.androidx.material3.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}