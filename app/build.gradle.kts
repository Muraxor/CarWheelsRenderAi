import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
}

project.extensions.configure<ApplicationExtension> {
    buildFeatures {
        compose = true
    }

    namespace = "car.wheels.renderai"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "car.wheels.renderai"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        debug {
            isDebuggable = true
        }
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

//    sourceSets {
//        getByName("main") {
//            java {
//                // Исключить конкретный файл
//                exclude("**/com/example/livecodeimproved/tbank/ScreenViewModelImproved.kt")
//                // Или по паттерну
//                exclude("**/tbank/ScreenViewModelImproved.kt")
//            }
//            kotlin {
//                exclude("**/com/example/livecodeimproved/tbank/ScreenViewModelImproved.kt")
//            }
//        }
//    }

}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    androidTestImplementation(project(":benchmark"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.coroutines)
    implementation(libs.retrofit)
    implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    platform(libs.compose.bom).apply {
        implementation(this)
        androidTestImplementation(this)
    }

    // Choose one of the following:
    // Material Design 3
    implementation(libs.androidx.material3)
    // or skip Material Design and build directly on top of foundational components
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.foundation.layout)
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    val media3Version = "1.3.0" // Use the latest stable version

    // Core ExoPlayer functionality
    implementation("androidx.media3:media3-exoplayer:$media3Version")

    // UI components (PlayerView)
    implementation("androidx.media3:media3-ui:$media3Version")

    // Optional: DASH streaming support
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version")


    implementation(project(":app:simplechatexample"))
}