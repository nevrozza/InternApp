package disk.components.flow

import auth.AuthComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import disk.components.page.DiskPageComponent
import kotlinx.serialization.Serializable
import utils.types.DiskPath

interface DiskComponent : BackHandlerOwner {
    val authComponent: AuthComponent
    val stack: Value<ChildStack<Disk.Config, Disk.Child>>

    fun onBackClicked()
}

interface Disk {
    @Serializable
    sealed interface Config {
        @Serializable
        data class Page(val path: DiskPath) : Config
    }

    sealed interface Child {
        data class Page(val component: DiskPageComponent) : Child
    }

}
