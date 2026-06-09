plugins {
    id("shared")
    id("androidx.room")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.core.storage.api)
            api(projects.shared.core.common)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines)

            implementation(libs.settings.core)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
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
