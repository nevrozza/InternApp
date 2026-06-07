plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}


dependencies {
    implementation(projects.shared.core.common)
    implementation(projects.shared.root.compose)
    implementation(projects.shared.utils)

    implementation(libs.koin.android)


    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    val config = Config.Android
    namespace = Config.namespace
    compileSdk = config.compileSdk

    defaultConfig {
        applicationId = Config.namespace
        minSdk = config.minSdk
        targetSdk = config.targetSdk
        with(Config.Version) {
            versionCode = code
            versionName = name
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        val javaVersion = JavaVersion.toVersion(Config.Java.javaVersionInt)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}