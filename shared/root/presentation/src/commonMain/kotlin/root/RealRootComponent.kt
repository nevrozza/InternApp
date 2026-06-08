package root

import auth.RealAuthComponent
import com.arkivanov.decompose.ComponentContext
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
        return listOf(Root.Config.Files)
    }

    override fun child(
        config: Root.Config,
        childCtx: ComponentContext
    ): Root.Child =
        when (config) {
            Root.Config.Files -> Root.Child.Files(
                RealAuthComponent(
                    componentContext = childCtx,
                    storeFactory = get(),
                    yandexOAuthUrlProvider = get(),
                    observeAuthEventsUseCase = get(),
                    observeAuthStateUseCase = get(),
                    refreshAuthStateUseCase = get(),
                    getYandexUserProfileUseCase = get(),
                    startYandexOAuthCallbackServerUseCase = get(),
                    stopYandexOAuthCallbackServerUseCase = get(),
                    logoutUseCase = get()
                )
            )
        }

}
