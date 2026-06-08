plugins {
    id("data-storage")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.auth.domain)
        }
    }
}
