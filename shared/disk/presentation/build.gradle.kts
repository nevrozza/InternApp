plugins {
    id("presentation")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.disk.domain)
            api(projects.shared.auth.presentation)
        }
    }
}
