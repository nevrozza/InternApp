plugins {
    id("compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.utils)
        }
    }
}

