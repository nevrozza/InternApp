plugins {
    id("presentation")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.network)
            implementation(projects.shared.core.storage.impl)
            implementation(projects.shared.auth.data)
            implementation(projects.shared.disk.data)

            implementation(projects.shared.auth.presentation)
            implementation(projects.shared.disk.presentation)

            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.logging)
        }
    }
}
