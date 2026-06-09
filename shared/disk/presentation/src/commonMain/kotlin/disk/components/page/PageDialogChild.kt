package disk.components.page

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.dismiss
import disk.dialogs.DiskDialogChild
import disk.dialogs.DiskDialogConfig
import disk.dialogs.RealCreateFolderDialogComponent
import disk.dialogs.RealCreateTextFileDialogComponent
import disk.dialogs.RealEditTextFileDialogComponent
import disk.dialogs.RealRenameResourceDialogComponent
import disk.mvi.DiskPageStore.Intent

fun RealDiskPageComponent.createDialogChild(
    config: DiskDialogConfig,
    childContext: ComponentContext,
): DiskDialogChild =
    when (config) {
        DiskDialogConfig.CreateFolder -> DiskDialogChild.CreateFolder(
            RealCreateFolderDialogComponent(
                componentContext = childContext,
                dismiss = dialogNav::dismiss,
                resourcesProvider = { model.value.items },
                nameValidator = nameValidator,
                confirm = { name ->
                    onEvent(Intent.CreateFolderConfirmed(name))
                    dialogNav.dismiss()
                },
            )
        )

        DiskDialogConfig.CreateTextFile -> DiskDialogChild.CreateTextFile(
            RealCreateTextFileDialogComponent(
                componentContext = childContext,
                dismiss = dialogNav::dismiss,
                resourcesProvider = { model.value.items },
                nameValidator = nameValidator,
                confirm = { name, content ->
                    onEvent(Intent.CreateTextFileConfirmed(name, content))
                    dialogNav.dismiss()
                },
            )
        )

        is DiskDialogConfig.RenameResource -> DiskDialogChild.RenameResource(
            RealRenameResourceDialogComponent(
                componentContext = childContext,
                dismiss = dialogNav::dismiss,
                currentName = config.currentName,
                currentLocalId = config.localId,
                resourcesProvider = { model.value.items },
                nameValidator = nameValidator,
                confirm = { name ->
                    val resource = model.value.items.firstOrNull { it.localId == config.localId }
                    if (resource == null) {
                        showError("Файл или папка уже изменились")
                    } else {
                        onEvent(Intent.RenameResourceConfirmed(resource, name))
                        dialogNav.dismiss()
                    }
                },
            )
        )

        is DiskDialogConfig.EditTextFile -> DiskDialogChild.EditTextFile(
            RealEditTextFileDialogComponent(
                componentContext = childContext,
                dismiss = dialogNav::dismiss,
                name = config.name,
                initialContent = config.content,
                confirm = { content ->
                    onEvent(Intent.SaveTextFileConfirmed(config.path, content))
                    dialogNav.dismiss()
                },
            )
        )
    }