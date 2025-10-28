import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-46"
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        // iosX64(), // Running on the iOS Simulator on Intel Macs (older)
        // iosArm64(), // Running on physical iOS devices
        iosSimulatorArm64() // Running on the iOS Simulator on Apple Silicon Macs (M1/M2/M3)
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.coroutines.core)

            api("com.rickclephas.kmp:kmp-observableviewmodel-core:1.0.0-BETA-14")

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kotlinx.datetime)

            implementation(libs.components.resources)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.assertk)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            //implementation(libs.koin.androidx.compose)
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "de.carlavoneicken.birthdaysapp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // this is added so that I can use java.time even with minsdk 24
        isCoreLibraryDesugaringEnabled = true
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    //add("kspIosX64", libs.room.compiler)
    //add("kspIosArm64", libs.room.compiler)

    // this is added so that I can use java.time even with minsdk 24
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}