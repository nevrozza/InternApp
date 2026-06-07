import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the

val libs = the<LibrariesForLibs>()
plugins {
    id("shared")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.decompose.compose)


            runtimeOnly(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)

            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

        }
        named("notIOSMain").dependencies {
            implementation(libs.compose.uiTooling)
        }
    }
}