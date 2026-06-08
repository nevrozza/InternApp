import gradle.kotlin.dsl.accessors._5d396f0e21e39e835a6ab853daed4dd5.kotlin
import gradle.kotlin.dsl.accessors._5d396f0e21e39e835a6ab853daed4dd5.sourceSets
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the

val libs = the<LibrariesForLibs>()



plugins {
    id("shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines)

            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}