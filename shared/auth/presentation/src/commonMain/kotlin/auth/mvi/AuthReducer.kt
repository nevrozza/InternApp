package auth.mvi

import auth.models.AuthState
import auth.mvi.AuthStore.Message
import auth.mvi.AuthStore.Status
import com.arkivanov.mvikotlin.core.store.Reducer

object AuthReducer : Reducer<AuthStore.State, Message> {
    override fun AuthStore.State.reduce(msg: Message): AuthStore.State =
        when (msg) {
            is Message.AuthStateChanged -> copy(status = msg.authState.toStatus())
            Message.AuthorizationStarted -> copy(status = Status.InProcess)
            is Message.Error -> copy(status = Status.Error(msg.msg))
        }
}

private fun AuthState.toStatus(): Status =
    when (this) {
        AuthState.Authorized -> Status.Authorized()
        AuthState.Unauthorized -> Status.Unauthorized
    }