package auth.mvi

import auth.desktopServer.StartYandexOAuthCallbackServerUseCase
import auth.desktopServer.StopYandexOAuthCallbackServerUseCase
import auth.models.AuthEvent
import auth.models.AuthState
import auth.mvi.AuthStore.Action
import auth.mvi.AuthStore.Intent
import auth.mvi.AuthStore.Label
import auth.mvi.AuthStore.Message
import auth.mvi.AuthStore.State
import auth.repositories.YandexOAuthUrlProvider
import auth.usecases.GetYandexUserProfileUseCase
import auth.usecases.LogoutUseCase
import auth.usecases.ObserveAuthEventsUseCase
import auth.usecases.ObserveAuthStateUseCase
import auth.usecases.RefreshAuthStateUseCase
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
    private val onAuthorizationRestored: suspend () -> Unit,
    private val onAuthorizedAfterLogin: suspend () -> Unit,
    private val onUnauthorized: suspend () -> Unit,
) :
    CoroutineExecutor<Intent, Action, State, Message, Label>() {

    private var isAuthorizationStarted = false

    override fun executeAction(action: Action) {
        when (action) {
            Action.ObserveAuth -> {
                refreshAuthStateUseCase()
                scope.launch {
                    val authStates = observeAuthStateUseCase()
                    var previousAuthState = authStates.value
                    var isFirstEmission = true

                    authStates
                        .collect { authState ->
                            dispatch(Message.AuthStateChanged(authState))

                            resolveAuthSideEffect(
                                previousAuthState = previousAuthState,
                                authState = authState,
                                isFirstEmission = isFirstEmission,
                                isAuthorizationStarted = isAuthorizationStarted,
                            )?.let { sideEffect ->
                                handleAuthSideEffects(sideEffect)
                                if (sideEffect == AuthSideEffect.AuthorizedAfterLogin) {
                                    isAuthorizationStarted = false
                                }
                            }

                            previousAuthState = authState
                            isFirstEmission = false

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
                isAuthorizationStarted = false
                dispatch(Message.AuthStateChanged(AuthState.Unauthorized))
            }
        }
    }

    private fun resolveAuthSideEffect(
        previousAuthState: AuthState,
        authState: AuthState,
        isFirstEmission: Boolean,
        isAuthorizationStarted: Boolean,
    ): AuthSideEffect? {
        return when (authState) {
            AuthState.Authorized -> when {
                isAuthorizationStarted -> AuthSideEffect.AuthorizedAfterLogin
                isFirstEmission -> AuthSideEffect.AuthorizationRestored
                previousAuthState == AuthState.Unauthorized -> AuthSideEffect.AuthorizationRestored
                else -> null
            }

            AuthState.Unauthorized -> when (previousAuthState) {
                AuthState.Authorized -> AuthSideEffect.Unauthorized
                AuthState.Unauthorized -> null
            }
        }
    }

    private fun handleAuthSideEffects(sideEffect: AuthSideEffect) {
        scope.launch {
            try {

                when (sideEffect) {
                    AuthSideEffect.AuthorizationRestored -> onAuthorizationRestored()
                    AuthSideEffect.AuthorizedAfterLogin -> onAuthorizedAfterLogin()
                    AuthSideEffect.Unauthorized -> onUnauthorized()
                }

            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                withContext(Dispatchers.IO) {
                    dispatch(Message.Error(error.message ?: "Auth side effect failed"))
                }
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
                isAuthorizationStarted = true
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

    private enum class AuthSideEffect {
        AuthorizationRestored,
        AuthorizedAfterLogin,
        Unauthorized,
    }
}
