package auth.mvi

import auth.models.AuthState
import auth.models.YandexUserProfile
import com.arkivanov.mvikotlin.core.store.Store

interface AuthStore : Store<AuthStore.Intent, AuthStore.State, AuthStore.Label> {

    data class State(
        val status: Status = Status.Unauthorized
    )

    sealed interface Status {
        data class Authorized(
            val profileData: YandexUserProfile? = null
        ) : Status

        data object Unauthorized : Status
        data object InProcess : Status
        data class Error(val msg: String) : Status
    }


    sealed interface Intent {
        data object YandexLoginClicked : Intent

        data object LogoutClicked : Intent

        data object CancelAuthorizationClicked : Intent
    }


    sealed interface Message {
        data class AuthStateChanged(val authState: AuthState) : Message
        data class ProfileLoaded(val profile: YandexUserProfile) : Message
        data object AuthorizationStarted : Message
        data class Error(val msg: String?) : Message
    }


    sealed interface Label {
        data class OpenYandexOAuth(val url: String) : Label
    }

    sealed interface Action {
        data object ObserveAuth : Action
    }
}
