plugins {
    id("data-storage")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.storage.impl)
        }
    }
}
