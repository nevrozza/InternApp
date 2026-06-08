import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime.annotation)

            implementation(libs.kotlinx.coroutines)
        }
    }
}