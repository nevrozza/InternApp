@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import com.android.build.api.withAndroid
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

val libs = the<LibrariesForLibs>()
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")

    id("com.android.kotlin.multiplatform.library")
    id("com.google.devtools.ksp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
    }

    android {
        namespace = Config.Android.namespace(project.path)
        compileSdk = Config.Android.compileSdk
        androidResources.enable = true
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()

    applyDefaultHierarchyTemplate {
        common {
            group("java") {
                withAndroid()
                withAndroidTarget()
                withJvm()
            }
        }
    }
}