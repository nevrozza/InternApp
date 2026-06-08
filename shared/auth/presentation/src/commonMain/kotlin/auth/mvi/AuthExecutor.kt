package auth.mvi

import auth.mvi.AuthStore.Action
import auth.mvi.AuthStore.Intent
import auth.mvi.AuthStore.Label
import auth.mvi.AuthStore.Message
import auth.mvi.AuthStore.State
import auth.models.AuthEvent
import auth.repositories.YandexOAuthUrlProvider
import auth.usecases.LogoutUseCase
import auth.usecases.ObserveAuthEventsUseCase
import auth.usecases.ObserveAuthStateUseCase
import auth.usecases.RefreshAuthStateUseCase
import auth.desktopServer.StartYandexOAuthCallbackServerUseCase
import auth.desktopServer.StopYandexOAuthCallbackServerUseCase
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch

internal class AuthExecutor(
    private val yandexOAuthUrlProvider: YandexOAuthUrlProvider,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val observeAuthEventsUseCase: ObserveAuthEventsUseCase,
    private val refreshAuthStateUseCase: RefreshAuthStateUseCase,
    private val startYandexOAuthCallbackServerUseCase: StartYandexOAuthCallbackServerUseCase,
    private val stopYandexOAuthCallbackServerUseCase: StopYandexOAuthCallbackServerUseCase,
    private val logoutUseCase: LogoutUseCase,
) :
    CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            Action.ObserveAuth -> {
                refreshAuthStateUseCase()
                scope.launch {
                    observeAuthStateUseCase().collect { authState ->
                        dispatch(Message.AuthStateChanged(authState))
                    }
                }
                scope.launch {
                    observeAuthEventsUseCase().collect { event ->
                        when (event) {
                            is AuthEvent.Error -> dispatch(Message.Error(event.msg))
                        }
                    }
                }
            }
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.YandexLoginClicked -> {
                scope.launch {
                    val url = yandexOAuthUrlProvider.getUrl()
                    if (url == null) {
                        dispatch(Message.Error("Yandex OAuth is not configured for this platform"))
                        return@launch
                    }

                    dispatch(Message.AuthorizationStarted)
                    startYandexOAuthCallbackServerUseCase()
                    publish(Label.OpenYandexOAuth(url))
                }
            }

            Intent.LogoutClicked -> {
                stopYandexOAuthCallbackServerUseCase()
                logoutUseCase()
            }

            Intent.CancelAuthorizationClicked -> {
                stopYandexOAuthCallbackServerUseCase()
                refreshAuthStateUseCase()
                publish(Label.CloseOAuth)
            }
        }
    }
}
