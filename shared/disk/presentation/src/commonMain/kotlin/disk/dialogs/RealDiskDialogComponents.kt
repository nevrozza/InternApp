package disk.dialogs

import com.arkivanov.decompose.ComponentContext
import disk.models.resources.DiskResource
import disk.validation.DiskNameValidationResult
import disk.validation.DiskNameValidator

abstract class BaseDiskDialogComponent(
    componentContext: ComponentContext,
    private val dismiss: () -> Unit,
) : ComponentContext by componentContext, DiskDialogComponent {

    override fun onDismiss() {
        dismiss()
    }
}

abstract class BaseNameDiskDialogComponent(
    componentContext: ComponentContext,
    dismiss: () -> Unit,
    private val resourcesProvider: () -> List<DiskResource>,
    private val nameValidator: DiskNameValidator,
    private val currentLocalId: String? = null,
) : BaseDiskDialogComponent(componentContext, dismiss) {

    protected fun validateResourceName(name: String): DiskNameValidationResult {
        return nameValidator.validate(
            name = name,
            resources = resourcesProvider(),
            currentLocalId = currentLocalId,
        )
    }
}

class RealCreateFolderDialogComponent(
    componentContext: ComponentContext,
    dismiss: () -> Unit,
    resourcesProvider: () -> List<DiskResource>,
    nameValidator: DiskNameValidator,
    private val confirm: (String) -> Unit,
) : BaseNameDiskDialogComponent(
    componentContext = componentContext,
    dismiss = dismiss,
    resourcesProvider = resourcesProvider,
    nameValidator = nameValidator,
), CreateFolderDialogComponent {

    override fun validateName(name: String): DiskNameValidationResult {
        return validateResourceName(name)
    }

    override fun onConfirm(name: String) {
        val result = validateName(name)
        if (result is DiskNameValidationResult.Valid) {
            confirm(result.name)
        }
    }
}

class RealCreateTextFileDialogComponent(
    componentContext: ComponentContext,
    dismiss: () -> Unit,
    resourcesProvider: () -> List<DiskResource>,
    nameValidator: DiskNameValidator,
    private val confirm: (String, String) -> Unit,
) : BaseNameDiskDialogComponent(
    componentContext = componentContext,
    dismiss = dismiss,
    resourcesProvider = resourcesProvider,
    nameValidator = nameValidator,
), CreateTextFileDialogComponent {

    override fun validateName(name: String): DiskNameValidationResult {
        return validateResourceName(name)
    }

    override fun onConfirm(name: String, content: String) {
        val result = validateName(name)
        if (result is DiskNameValidationResult.Valid) {
            confirm(result.name, content)
        }
    }
}

class RealRenameResourceDialogComponent(
    componentContext: ComponentContext,
    dismiss: () -> Unit,
    override val currentName: String,
    currentLocalId: String,
    resourcesProvider: () -> List<DiskResource>,
    nameValidator: DiskNameValidator,
    private val confirm: (String) -> Unit,
) : BaseNameDiskDialogComponent(
    componentContext = componentContext,
    dismiss = dismiss,
    resourcesProvider = resourcesProvider,
    nameValidator = nameValidator,
    currentLocalId = currentLocalId,
), RenameResourceDialogComponent {

    override fun validateName(name: String): DiskNameValidationResult {
        return validateResourceName(name)
    }

    override fun onConfirm(name: String) {
        val result = validateName(name)
        if (result is DiskNameValidationResult.Valid) {
            confirm(result.name)
        }
    }
}

class RealEditTextFileDialogComponent(
    componentContext: ComponentContext,
    dismiss: () -> Unit,
    override val name: String,
    override val initialContent: String,
    private val confirm: (String) -> Unit,
) : BaseDiskDialogComponent(componentContext, dismiss), EditTextFileDialogComponent {

    override fun onConfirm(content: String) {
        confirm(content)
    }
}
