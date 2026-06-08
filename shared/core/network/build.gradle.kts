plugins {
    id("shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(libs.ktor.client.cio)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.content.negotiation)
        }
    }
}