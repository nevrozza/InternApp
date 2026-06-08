package auth

import auth.mvi.AuthExecutor
import auth.mvi.AuthReducer
import auth.mvi.AuthStore.Action
import auth.mvi.AuthStore.Intent
import auth.mvi.AuthStore.Label
import auth.mvi.AuthStore.State
import auth.repositories.YandexOAuthUrlProvider
import auth.usecases.LogoutUseCase
import auth.usecases.ObserveAuthEventsUseCase
import auth.usecases.ObserveAuthStateUseCase
import auth.usecases.RefreshAuthStateUseCase
import auth.desktopServer.StartYandexOAuthCallbackServerUseCase
import auth.desktopServer.StopYandexOAuthCallbackServerUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.StoreFactory
import utils.presentation.components.DefaultMVIComponent

class RealAuthComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    yandexOAuthUrlProvider: YandexOAuthUrlProvider,
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    observeAuthEventsUseCase: ObserveAuthEventsUseCase,
    refreshAuthStateUseCase: RefreshAuthStateUseCase,
    startYandexOAuthCallbackServerUseCase: StartYandexOAuthCallbackServerUseCase,
    stopYandexOAuthCallbackServerUseCase: StopYandexOAuthCallbackServerUseCase,
    logoutUseCase: LogoutUseCase,
) : AuthComponent,
    DefaultMVIComponent<Intent, State, Label>(
        componentContext = componentContext,
        storeFactory = {
            storeFactory.create(
                name = "AuthStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Action.ObserveAuth),
                executorFactory = {
                    AuthExecutor(
                        yandexOAuthUrlProvider = yandexOAuthUrlProvider,
                        observeAuthStateUseCase = observeAuthStateUseCase,
                        observeAuthEventsUseCase = observeAuthEventsUseCase,
                        refreshAuthStateUseCase = refreshAuthStateUseCase,
                        startYandexOAuthCallbackServerUseCase = startYandexOAuthCallbackServerUseCase,
                        stopYandexOAuthCallbackServerUseCase = stopYandexOAuthCallbackServerUseCase,
                        logoutUseCase = logoutUseCase,
                    )
                },
                reducer = AuthReducer,
            )
        }
    ) {

    override fun onYandexLoginClicked() {
        onEvent(Intent.YandexLoginClicked)
    }

    override fun onLogoutClicked() {
        onEvent(Intent.LogoutClicked)
    }

    override fun onCancelAuthorizationClicked() {
        onEvent(Intent.CancelAuthorizationClicked)
    }
}
