plugins {
    id("data-storage")
    id("data-network")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.storage.impl)
            implementation(projects.shared.core.network)

            // transactions
            implementation(libs.room.runtime)

            api(projects.shared.disk.domain)
        }
    }
}
