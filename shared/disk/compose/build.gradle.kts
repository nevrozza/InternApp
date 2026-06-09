plugins {
    id("compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.auth.compose)
            api(projects.shared.disk.presentation)

            implementation(projects.shared.utils.compose)
        }
    }
}

