package disk.dialogs

import disk.validation.DiskNameValidationResult

sealed interface DiskDialogChild {
    data class CreateFolder(val component: CreateFolderDialogComponent) : DiskDialogChild
    data class CreateTextFile(val component: CreateTextFileDialogComponent) : DiskDialogChild
    data class RenameResource(val component: RenameResourceDialogComponent) : DiskDialogChild
    data class EditTextFile(val component: EditTextFileDialogComponent) : DiskDialogChild
}

interface DiskDialogComponent {
    fun onDismiss()
}

interface CreateFolderDialogComponent : DiskDialogComponent {
    fun validateName(name: String): DiskNameValidationResult
    fun onConfirm(name: String)
}

interface CreateTextFileDialogComponent : DiskDialogComponent {
    fun validateName(name: String): DiskNameValidationResult
    fun onConfirm(name: String, content: String)
}

interface RenameResourceDialogComponent : DiskDialogComponent {
    val currentName: String

    fun validateName(name: String): DiskNameValidationResult
    fun onConfirm(name: String)
}

interface EditTextFileDialogComponent : DiskDialogComponent {
    val name: String
    val initialContent: String

    fun onConfirm(content: String)
}
