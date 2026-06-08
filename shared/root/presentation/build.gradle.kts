plugins {
    id("presentation")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.storage.impl)
            implementation(projects.shared.auth.data)

            implementation(projects.shared.auth.presentation)

            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.logging)
        }
    }
}