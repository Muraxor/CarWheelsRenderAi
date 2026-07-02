import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.android.application)
    //id("com.android.library")
    alias(libs.plugins.android.built.in1.kotlin)
    id("androidx.benchmark") version "1.4.1" apply false
    alias(libs.plugins.compose.compiler)
}

project.extensions.configure<ApplicationExtension> {
    namespace = "com.example.benchmark"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        compose = true
    }

    defaultConfig {
        //applicationId = "com.example.benchmark"
        minSdk = 24
        //targetSdk = 36
        //versionCode = 1
        //versionName = "1.0"

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR,DEBUGGABLE"

        // ОТКЛЮЧАЕМ Orchestrator
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        manifestPlaceholders["androidx.benchmark.enableUiAutomation"] = "true"
    }


    testOptions {
        // Разрешаем запуск бенчмарков на эмуляторе
        //execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = false
        }

        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug") // Если нет release ключа
            //isDebuggable = false
            enableUnitTestCoverage = true


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
}

dependencies {
    // Доступ к основному модулю (твоему приложению)
    androidTestImplementation(project(":app"))

    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    implementation(libs.androidx.benchmark.junit4) // ЯВНО
    androidTestImplementation(libs.androidx.benchmark.macro.junit4)
    androidTestImplementation(libs.androidx.uiautomator)

    // Compose-тесты (чтобы находить ComposeView)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.ui.test.manifest)
}