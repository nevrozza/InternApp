plugins {
    id("shared")
    id("androidx.room")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.storage.api)
            implementation(libs.koin.core)

            implementation(libs.settings.core)

            implementation(libs.room.runtime)
        }

        androidMain.dependencies {
            implementation(libs.androidx.security.crypto)
        }
    }
}

dependencies {
    ksp(libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}