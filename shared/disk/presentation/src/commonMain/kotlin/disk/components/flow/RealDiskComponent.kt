package disk.components.flow

import auth.AuthComponent
import com.arkivanov.decompose.ComponentContext
import disk.components.flow.DiskComponent.Companion.RootPath
import disk.components.page.DiskPageComponent
import disk.components.page.RealDiskPageComponent
import disk.validation.DiskNameValidator
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import utils.presentation.navigation.DefaultStack

class RealDiskComponent(
    componentContext: ComponentContext,
    override val authComponent: AuthComponent,
) : DiskComponent,
    KoinComponent,
    DefaultStack<Disk.Config, Disk.Child>(
        componentContext = componentContext,
        serializer = Disk.Config.serializer(),
    ) {

    override fun initialConfig(): List<Disk.Config> {
        return listOf(Disk.Config.Page(RootPath))
    }

    override fun child(
        config: Disk.Config,
        childCtx: ComponentContext,
    ): Disk.Child =
        when (config) {
            is Disk.Config.Page -> Disk.Child.Page(
                RealDiskPageComponent(
                    componentContext = childCtx,
                    storeFactory = get(),
                    path = config.path,
                    diskUseCases = get(),
                    nameValidator = DiskNameValidator(),
                    output = ::onPageOutput,
                )
            )
        }

    private fun onPageOutput(output: DiskPageComponent.Output) {
        when (output) {
            is DiskPageComponent.Output.NavigateToDirectory -> openNew(Disk.Config.Page(output.path))
            DiskPageComponent.Output.NavigateBack -> popOnce(Disk.Child.Page::class)

        }
    }
}
