@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl


plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
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
}