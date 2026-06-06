rootProject.name = "InternApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":apps:androidApp")
include(":apps:desktopApp")
include(":apps:ios:iosKMP")

include(":shared:core:network:api")
include(":shared:core:network:impl")
include(":shared:core:storage:api")
include(":shared:core:storage:impl")

include(":shared:auth:domain")
include(":shared:auth:data")
include(":shared:auth:presentation")
include(":shared:auth:compose")

include(":shared:disk:domain")
include(":shared:disk:data")
include(":shared:disk:presentation")
include(":shared:disk:compose")

include(":shared:utils")
include(":shared:utils:pure")