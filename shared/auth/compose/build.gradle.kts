plugins {
    id("compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.auth.presentation)
            implementation(projects.shared.utils.compose)
        }
    }
}