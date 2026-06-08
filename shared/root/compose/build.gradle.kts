plugins {
    id("compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.root.presentation)

            implementation(projects.shared.auth.compose)
        }
    }
}