plugins {
    id("data-storage")
    id("data-network")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.shared.auth.domain)
        }
    }
}
