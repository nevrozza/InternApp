plugins {
    id("data-storage")
    id("data-network")
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.network)
            implementation(libs.ktor.client.auth)

            api(projects.shared.auth.domain)
            implementation(projects.shared.utils.pure)
        }
    }
}
