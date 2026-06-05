@file:Suppress(
    "SpellCheckingInspection",
    "MemberVisibilityCanBePrivate",
    "ConstPropertyName"
)

import org.gradle.api.JavaVersion

object Config {
    const val namespace = "org.nevrzq.intern"

    object Version {
        const val code = 1
        const val name = "1.0.0"
    }

    object Android {
        const val compileSdk = 36
        const val minSdk = 26
        const val targetSdk = 36

        private const val sharedNamespace = "$namespace.shared"

        fun namespace(module: String?): String {
            print(module)
            val formattedModuleName = module?.let {
                module.replace(":shared", "")
                    .replace(Regex("[:-]"), ".")
            } ?: ""
            return "$sharedNamespace$formattedModuleName"
        }
    }

    object Java {
        const val javaVersionInt = 21
        val javaVersionGradle = JavaVersion.VERSION_21
    }
}