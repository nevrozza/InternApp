package disk.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import disk.mvi.DiskPageStore.Message
import disk.mvi.DiskPageStore.SyncIndicatorState

object DiskPageReducer : Reducer<DiskPageStore.State, Message> {
    override fun DiskPageStore.State.reduce(msg: Message): DiskPageStore.State =
        when (msg) {
            is Message.ItemsChanged -> copy(items = msg.items)
            is Message.RefreshingChanged -> copy(isRefreshing = msg.isRefreshing)
            is Message.CreateMenuVisibilityChanged -> copy(isCreateMenuVisible = msg.isVisible)
            is Message.ResourceMenuTargetChanged -> copy(resourceMenuTarget = msg.resource)
            is Message.SyncOperationsChanged -> copy(
                syncOperations = msg.syncOperations,
                syncIndicatorState = msg.syncOperations.toIndicatorState()
            )
        }


    private fun List<SyncOperation>.toIndicatorState(): SyncIndicatorState {
        if (isEmpty()) return SyncIndicatorState.Hidden

        val hasRunning = any {
            it.state == SyncOperationState.RUNNING ||
                    it.state == SyncOperationState.PENDING
        }

        val hasFailed = any {
            it.state == SyncOperationState.FAILED
        }

        return when {
            hasRunning && hasFailed -> SyncIndicatorState.SyncingWithErrors
            hasRunning -> SyncIndicatorState.Syncing
            hasFailed -> SyncIndicatorState.Failed
            else -> SyncIndicatorState.Hidden
        }
    }
}

