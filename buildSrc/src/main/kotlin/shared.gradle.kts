@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import com.android.build.api.withAndroid
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")

    id("com.android.kotlin.multiplatform.library")
    id("com.google.devtools.ksp")
}

kotlin {
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