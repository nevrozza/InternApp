package disk.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import disk.models.resources.DiskResource
import disk.mvi.DiskPageStore.Action
import disk.mvi.DiskPageStore.Intent
import disk.mvi.DiskPageStore.Label
import disk.mvi.DiskPageStore.Message
import disk.mvi.DiskPageStore.State
import disk.usecases.DiskUseCases
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.types.DiskPath

internal class DiskPageExecutor(
    private val diskUseCases: DiskUseCases,
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            Action.ObservePage -> {
                scope.launch {
                    diskUseCases.observeDirectory(state().currentPath).collect { resources ->
                        dispatch(Message.ItemsChanged(resources))
                    }
                }
                refresh()
            }
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.Refresh -> refresh()
            Intent.CreateMenuClicked -> dispatch(Message.CreateMenuVisibilityChanged(true))
            Intent.CreateMenuDismissed -> dispatch(Message.CreateMenuVisibilityChanged(false))
            is Intent.ResourceMenuRequested -> dispatch(Message.ResourceMenuTargetChanged(intent.resource))
            Intent.ResourceMenuDismissed -> dispatch(Message.ResourceMenuTargetChanged(null))
            is Intent.CreateFolderConfirmed -> createFolder(intent.name)
            is Intent.CreateTextFileConfirmed -> createTextFile(intent.name, intent.content)
            is Intent.DeleteResourceConfirmed -> deleteResource(intent.resource)
            is Intent.RenameResourceConfirmed -> renameResource(intent.resource, intent.name)
            is Intent.SaveTextFileConfirmed -> saveTextFile(intent.path, intent.content)
            is Intent.ShowError -> publish(Label.ShowError(intent.message))
        }
    }

    private fun refresh() {
        scope.launch {
            dispatch(Message.RefreshingChanged(true))
            runDiskOperation {
                diskUseCases.refreshDirectory(state().currentPath)
            }
            dispatch(Message.RefreshingChanged(false))
        }
    }

    private fun createFolder(name: String) {
        scope.launch {
            runDiskOperation {
                diskUseCases.createFolder(
                    parentPath = state().currentPath,
                    name = name,
                )
                diskUseCases.pushSyncDisk()
            }
        }
    }

    private fun createTextFile(name: String, content: String) {
        scope.launch {
            runDiskOperation {
                diskUseCases.saveTextFile(
                    path = state().currentPath.child(name),
                    content = content,
                )
                diskUseCases.pushSyncDisk()
            }
        }
    }

    private fun deleteResource(resource: DiskResource) {
        scope.launch {
            dispatch(Message.ResourceMenuTargetChanged(null))
            runDiskOperation {
                diskUseCases.deleteResource(resource.path)
                diskUseCases.pushSyncDisk()
            }
        }
    }

    private fun renameResource(resource: DiskResource, name: String) {
        scope.launch {
            dispatch(Message.ResourceMenuTargetChanged(null))
            runDiskOperation {
                diskUseCases.renameResource(
                    sourcePath = resource.path,
                    newName = name,
                )
                diskUseCases.pushSyncDisk()
            }
        }
    }

    private fun saveTextFile(path: DiskPath, content: String) {
        scope.launch {
            runDiskOperation {
                diskUseCases.saveTextFile(
                    path = path,
                    content = content,
                )
                diskUseCases.pushSyncDisk()
            }
        }
    }

    private suspend fun runDiskOperation(block: suspend () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                block()
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            publish(Label.ShowError(error.message ?: "Disk operation failed"))
        }
    }
}
