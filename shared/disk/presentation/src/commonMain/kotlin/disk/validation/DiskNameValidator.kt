package disk.validation

import disk.models.resources.DiskResource

class DiskNameValidator {

    fun validate(
        name: String,
        resources: List<DiskResource>,
        currentLocalId: String? = null,
    ): DiskNameValidationResult {
        val trimmedName = name.trim()

        return when {
            trimmedName.isBlank() -> DiskNameValidationResult.Invalid.Empty
            trimmedName.contains('/') -> DiskNameValidationResult.Invalid.ContainsSlash
            resources.any { resource ->
                resource.localId != currentLocalId && resource.name == trimmedName
            } -> DiskNameValidationResult.Invalid.AlreadyExists
            else -> DiskNameValidationResult.Valid(trimmedName)
        }
    }
}

sealed interface DiskNameValidationResult {
    data class Valid(val name: String) : DiskNameValidationResult

    sealed interface Invalid : DiskNameValidationResult {
        data object Empty : Invalid
        data object ContainsSlash : Invalid
        data object AlreadyExists : Invalid
    }
}
