package root

import auth.AuthComponent
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable

interface RootComponent: BackHandlerOwner {
    val stack: Value<ChildStack<Root.Config, Root.Child>>

    fun onBackClicked()
}


interface Root {

    @Serializable
    sealed interface Config {
        @Serializable
        data object Files : Config

        @Serializable
        data object Settings : Config
    }

    sealed interface Child {
        data class Files(
            val component: AuthComponent
        ) : Child

        data class Settings(
            val component: Any
        ) : Child
    }
}