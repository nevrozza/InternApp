plugins {
    id("presentation")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.storage.impl)

            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.logging)
        }
    }
}