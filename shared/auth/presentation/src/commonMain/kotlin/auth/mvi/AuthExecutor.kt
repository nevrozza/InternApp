package auth.mvi

import auth.mvi.AuthStore.Action
import auth.mvi.AuthStore.Intent
import auth.mvi.AuthStore.Label
import auth.mvi.AuthStore.Message
import auth.mvi.AuthStore.State
import auth.models.AuthEvent
import auth.models.AuthState
import auth.repositories.YandexOAuthUrlProvider
import auth.usecases.GetYandexUserProfileUseCase
import auth.usecases.LogoutUseCase
import auth.usecases.ObserveAuthEventsUseCase
import auth.usecases.ObserveAuthStateUseCase
import auth.usecases.RefreshAuthStateUseCase
import auth.desktopServer.StartYandexOAuthCallbackServerUseCase
import auth.desktopServer.StopYandexOAuthCallbackServerUseCase
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AuthExecutor(
    private val yandexOAuthUrlProvider: YandexOAuthUrlProvider,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val observeAuthEventsUseCase: ObserveAuthEventsUseCase,
    private val refreshAuthStateUseCase: RefreshAuthStateUseCase,
    private val getYandexUserProfileUseCase: GetYandexUserProfileUseCase,
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
                        if (authState == AuthState.Authorized) {
                            loadYandexProfile()
                        }
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
            Intent.YandexLoginClicked -> yandexLogin()

            Intent.LogoutClicked -> scope.launch(Dispatchers.IO) {
                logoutUseCase()
                stopYandexOAuthCallbackServerUseCase()
            }

            Intent.CancelAuthorizationClicked -> scope.launch {
                withContext(Dispatchers.IO) {
                    refreshAuthStateUseCase()
                    stopYandexOAuthCallbackServerUseCase()
                }
                publish(Label.CloseOAuth)
            }
        }
    }

    private fun yandexLogin() {
        if (state().status !is AuthStore.Status.Authorized) {
            scope.launch {
                val url = yandexOAuthUrlProvider.getUrl()
                if (url == null) {
                    dispatch(Message.Error("Yandex OAuth is not configured for this platform"))
                    return@launch
                }
                dispatch(Message.AuthorizationStarted)
                withContext(Dispatchers.IO) {
                    startYandexOAuthCallbackServerUseCase()
                }
                publish(Label.OpenYandexOAuth(url))
            }
        }
    }

    private suspend fun loadYandexProfile() {
        try {
            getYandexUserProfileUseCase().collect { profile ->
                dispatch(Message.ProfileLoaded(profile))
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            dispatch(Message.Error(error.message ?: "Unable to load Yandex profile"))
        }
    }
}
