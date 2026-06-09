package disk.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import disk.mvi.DiskPageStore.Message

object DiskPageReducer : Reducer<DiskPageStore.State, Message> {
    override fun DiskPageStore.State.reduce(msg: Message): DiskPageStore.State =
        when (msg) {
            is Message.ItemsChanged -> copy(items = msg.items)
            is Message.RefreshingChanged -> copy(isRefreshing = msg.isRefreshing)
            is Message.CreateMenuVisibilityChanged -> copy(isCreateMenuVisible = msg.isVisible)
            is Message.ResourceMenuTargetChanged -> copy(resourceMenuTarget = msg.resource)
        }
}
