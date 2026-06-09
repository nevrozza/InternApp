package disk.mvi

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import disk.models.resources.DiskResource
import disk.models.sync.SyncOperation
import utils.types.DiskPath

interface DiskPageStore : Store<DiskPageStore.Intent, DiskPageStore.State, DiskPageStore.Label> {

    data class State(
        val currentPath: DiskPath,
        val items: List<DiskResource> = emptyList(),
        val isRefreshing: Boolean = false,
        val isCreateMenuVisible: Boolean = false,
        val resourceMenuTarget: DiskResource? = null,
        val syncOperations: List<SyncOperation> = emptyList(),
        val syncIndicatorState: SyncIndicatorState = SyncIndicatorState.Hidden,
    )

    @Immutable
    sealed interface SyncIndicatorState {
        data object Hidden : SyncIndicatorState

        data object Syncing : SyncIndicatorState

        data object Failed : SyncIndicatorState

        data object SyncingWithErrors : SyncIndicatorState
    }

    sealed interface Intent {
        data object Refresh : Intent

        data object CreateMenuClicked : Intent
        data object CreateMenuDismissed : Intent

        data class ResourceMenuRequested(val resource: DiskResource) : Intent
        data object ResourceMenuDismissed : Intent

        data class CreateFolderConfirmed(val name: String) : Intent
        data class CreateTextFileConfirmed(val name: String, val content: String) : Intent
        data class DeleteResourceConfirmed(val resource: DiskResource) : Intent
        data class RenameResourceConfirmed(val resource: DiskResource, val name: String) : Intent
        data class SaveTextFileConfirmed(val path: DiskPath, val content: String) : Intent

        data class ShowError(val message: String) : Intent

        data object OnSyncClicked : Intent
        data class CancelLocalSyncConfirmed(val operation: SyncOperation) : Intent
    }

    sealed interface Message {
        data class ItemsChanged(val items: List<DiskResource>) : Message
        data class RefreshingChanged(val isRefreshing: Boolean) : Message
        data class CreateMenuVisibilityChanged(val isVisible: Boolean) : Message
        data class ResourceMenuTargetChanged(val resource: DiskResource?) : Message

        data class SyncOperationsChanged(val syncOperations: List<SyncOperation>) : Message
    }

    sealed interface Label {
        data class ShowError(val message: String) : Label
    }

    sealed interface Action {
        data object ObservePage : Action
        data object ObserveSyncOps : Action
    }
}
