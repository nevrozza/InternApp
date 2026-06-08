import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.decompose.core)
            implementation(libs.mvikotlin.core)
            implementation(libs.mvikotlin.coroutines)

            implementation(libs.koin.core)

            implementation(libs.compose.runtime.annotation)

            implementation(libs.kotlinx.coroutines)

            implementation(project(":shared:utils"))
        }
    }

    compilerOptions {
        optIn.addAll(
            "com.arkivanov.decompose.ExperimentalDecomposeApi"
        )
    }
}