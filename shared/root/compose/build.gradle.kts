plugins {
    id("compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.root.presentation)
        }
    }
}