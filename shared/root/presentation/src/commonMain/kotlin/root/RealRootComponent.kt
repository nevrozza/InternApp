package root

import auth.RealAuthComponent
import com.arkivanov.decompose.ComponentContext
import disk.components.flow.DiskComponent
import disk.components.flow.RealDiskComponent
import disk.usecases.DiskUseCases
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import utils.presentation.navigation.DefaultStack


class RealRootComponent(
    componentContext: ComponentContext
) : RootComponent, KoinComponent, DefaultStack<Root.Config, Root.Child>(
    componentContext = componentContext,
    serializer = Root.Config.serializer()
) {
    override fun initialConfig(): List<Root.Config> {
        return listOf(Root.Config.Disk)
    }

    override fun child(
        config: Root.Config,
        childCtx: ComponentContext
    ): Root.Child {
        return when (config) {
            Root.Config.Disk -> {
                val diskUseCases = get<DiskUseCases>()

                Root.Child.Disk(
                    RealDiskComponent(
                        componentContext = childCtx,
                        authComponent = RealAuthComponent(
                            componentContext = childCtx,
                            storeFactory = get(),
                            yandexOAuthUrlProvider = get(),
                            observeAuthEventsUseCase = get(),
                            observeAuthStateUseCase = get(),
                            refreshAuthStateUseCase = get(),
                            getYandexUserProfileUseCase = get(),
                            startYandexOAuthCallbackServerUseCase = get(),
                            stopYandexOAuthCallbackServerUseCase = get(),
                            logoutUseCase = get(),
                            onAuthorizationRestored = {
                                diskUseCases.pushSyncDisk()
                                diskUseCases.refreshDirectory(DiskComponent.RootPath)
                            },
                            onAuthorizedAfterLogin = {
                                diskUseCases.clearDiskCache()
                                diskUseCases.pushSyncDisk()
                                diskUseCases.refreshDirectory(DiskComponent.RootPath)
                            },
                            onUnauthorized = {
                                diskUseCases.clearDiskCache()
                            },
                        )
                    )
                )
            }
        }
    }

}
