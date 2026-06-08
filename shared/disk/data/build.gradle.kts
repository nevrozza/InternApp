plugins {
    id("data-storage")
    id("data-network")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.storage.impl)
            implementation(projects.shared.core.network)

            api(projects.shared.disk.domain)
        }
    }
}
