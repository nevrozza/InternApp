import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.core.common)
            implementation(projects.shared.root.compose)

            implementation(libs.koin.core)

            implementation(libs.decompose.compose)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(projects.shared.auth.domain)
            implementation(projects.shared.auth.data)
            implementation(projects.shared.utils.pure)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
        }
    }

}
compose.desktop {
    application {
        mainClass = Config.namespace + ".MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = Config.namespace
            packageVersion = Config.Version.name
        }
    }
}
