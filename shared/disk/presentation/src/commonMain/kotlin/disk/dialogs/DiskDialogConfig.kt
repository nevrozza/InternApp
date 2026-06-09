package disk.dialogs

import kotlinx.serialization.Serializable
import utils.types.DiskPath



@Serializable
sealed interface DiskDialogConfig {
    @Serializable
    data object CreateFolder : DiskDialogConfig

    @Serializable
    data object CreateTextFile : DiskDialogConfig

    @Serializable
    data class RenameResource(
        val localId: String,
        val currentName: String,
    ) : DiskDialogConfig

    @Serializable
    data class EditTextFile(
        val path: DiskPath,
        val name: String,
        val content: String,
    ) : DiskDialogConfig
}
