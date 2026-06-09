package disk.components.page

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.StoreFactory
import disk.dialogs.DiskDialogConfig
import disk.models.resources.BinaryFileResource
import disk.models.resources.DirectoryResource
import disk.models.resources.DiskResource
import disk.models.resources.TextFileResource
import disk.models.sync.SyncOperation
import disk.mvi.DiskPageExecutor
import disk.mvi.DiskPageReducer
import disk.mvi.DiskPageStore.Action
import disk.mvi.DiskPageStore.Intent
import disk.mvi.DiskPageStore.Label
import disk.mvi.DiskPageStore.State
import disk.usecases.DiskUseCases
import disk.validation.DiskNameValidator
import utils.presentation.components.DefaultMVIComponent
import utils.types.DiskPath

class RealDiskPageComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    path: DiskPath,
    diskUseCases: DiskUseCases,
    val nameValidator: DiskNameValidator,
    private val output: (DiskPageComponent.Output) -> Unit,
) : DiskPageComponent,
    DefaultMVIComponent<Intent, State, Label>(
        componentContext = componentContext,
        storeFactory = {
            storeFactory.create(
                name = "DiskPageStore",
                initialState = State(currentPath = path),
                bootstrapper = SimpleBootstrapper(Action.ObservePage, Action.ObserveSyncOps),
                executorFactory = {
                    DiskPageExecutor(diskUseCases = diskUseCases)
                },
                reducer = DiskPageReducer,
            )
        }
    ) {

    val dialogNav = SlotNavigation<DiskDialogConfig>()

    private val createMenuBackCallback = BackCallback(isEnabled = false) {
        onCreateMenuDismissed()
    }

    init {
        backHandler.register(createMenuBackCallback)
        lifecycle.subscribe(
            onDestroy = {
                backHandler.unregister(createMenuBackCallback)
            },
        )
    }

    override val dialogSlot = childSlot(
        source = dialogNav,
        serializer = DiskDialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createDialogChild,
    )

    override fun onRefresh() {
        onEvent(Intent.Refresh)
    }

    override fun onResourceClicked(resource: DiskResource) {
        when (resource) {
            is DirectoryResource -> output(DiskPageComponent.Output.NavigateToDirectory(resource.path))
            is TextFileResource -> onEditTextClicked(resource)
            is BinaryFileResource -> showError("Файл можно только переименовать или удалить")
        }
    }

    override fun onBackClicked() {
        output(DiskPageComponent.Output.NavigateBack)
    }

    override fun onCreateMenuClicked() {
        createMenuBackCallback.isEnabled = true
        onEvent(Intent.CreateMenuClicked)
    }

    override fun onCreateMenuDismissed() {
        createMenuBackCallback.isEnabled = false
        onEvent(Intent.CreateMenuDismissed)
    }

    override fun onCreateFolderClicked() {
        onCreateMenuDismissed()
        dialogNav.activate(DiskDialogConfig.CreateFolder)
    }

    override fun onCreateTextFileClicked() {
        onCreateMenuDismissed()
        dialogNav.activate(DiskDialogConfig.CreateTextFile)
    }

    override fun onUploadFileClicked() {
        onCreateMenuDismissed()
        showError("Загрузка файла пока заглушка")
    }

    override fun onSyncClicked() {
        onEvent(Intent.OnSyncClicked)
    }

    override fun onCancelLocalSyncClicked(operation: SyncOperation) {
        onEvent(Intent.CancelLocalSyncConfirmed(operation))
    }

    override fun onResourceMenuRequested(resource: DiskResource) {
        onEvent(Intent.ResourceMenuRequested(resource))
    }

    override fun onResourceMenuDismissed() {
        onEvent(Intent.ResourceMenuDismissed)
    }

    override fun onRenameClicked(resource: DiskResource) {
        onEvent(Intent.ResourceMenuDismissed)
        dialogNav.activate(
            DiskDialogConfig.RenameResource(
                localId = resource.localId,
                currentName = resource.name,
            )
        )
    }

    override fun onDeleteClicked(resource: DiskResource) {
        onEvent(Intent.DeleteResourceConfirmed(resource))
    }

    override fun onEditTextClicked(resource: TextFileResource) {
        onEvent(Intent.ResourceMenuDismissed)
        dialogNav.activate(
            DiskDialogConfig.EditTextFile(
                path = resource.path,
                name = resource.name,
                content = resource.textContent,
            )
        )
    }

    fun showError(message: String) {
        onEvent(Intent.ShowError(message))
    }
}
