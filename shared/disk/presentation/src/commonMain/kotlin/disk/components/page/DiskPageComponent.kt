package disk.components.page

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import disk.dialogs.DiskDialogChild
import disk.dialogs.DiskDialogConfig
import disk.models.resources.DiskResource
import disk.models.resources.TextFileResource
import disk.mvi.DiskPageStore.State
import disk.mvi.DiskPageStore.Label
import kotlinx.coroutines.flow.Flow
import utils.types.DiskPath

interface DiskPageComponent {
    val model: Value<State>
    val labels: Flow<Label>
    val dialogSlot: Value<ChildSlot<DiskDialogConfig, DiskDialogChild>>

    fun onRefresh()

    fun onResourceClicked(resource: DiskResource)

    fun onBackClicked()

    fun onCreateMenuClicked()
    fun onCreateMenuDismissed()

    fun onCreateFolderClicked()
    fun onCreateTextFileClicked()
    fun onUploadFileClicked()

    fun onResourceMenuRequested(resource: DiskResource)
    fun onResourceMenuDismissed()

    fun onRenameClicked(resource: DiskResource)
    fun onDeleteClicked(resource: DiskResource)
    fun onEditTextClicked(resource: TextFileResource)

    sealed interface Output {
        data class NavigateToDirectory(val path: DiskPath) : Output
        data object NavigateBack : Output
    }
}
