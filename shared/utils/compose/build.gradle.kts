plugins {
    id("compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.utils)
            implementation(projects.shared.utils.pure)

            implementation(libs.bundles.coil)
        }
    }
}

