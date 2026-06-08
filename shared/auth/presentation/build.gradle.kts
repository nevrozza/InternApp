plugins {
    id("presentation")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.auth.domain)
        }
    }
}
