plugins {
    id("shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.content.negotiation)
        }

        javaMain.dependencies {
            implementation(libs.ktor.client.engine.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.engine.darwin)
        }
    }
}