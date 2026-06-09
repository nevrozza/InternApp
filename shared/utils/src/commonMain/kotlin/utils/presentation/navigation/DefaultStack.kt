package utils.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

abstract class DefaultStack<Config : Any, Child : Any>(
    componentContext: ComponentContext,
    serializer: KSerializer<Config>,
) : ComponentContext by componentContext, BackHandlerOwner {
    private val nav: StackNavigation<Config> = StackNavigation()
    val stack: Value<ChildStack<Config, Child>> = childStack(
        source = nav,
        serializer = serializer,
        initialStack = ::initialConfig,
        childFactory = ::child,
        handleBackButton = true
    )

    protected abstract fun initialConfig(): List<Config>
    protected abstract fun child(
        config: Config,
        childCtx: ComponentContext
    ): Child

    fun openSingle(config: Config) =
        nav.bringToFront(config)

    fun openNew(config: Config) =
        nav.pushNew(config)

    fun replace(config: Config) =
        nav.replaceCurrent(config)

    fun replaceAll(config: Config) =
        nav.replaceAll(config)


    @Suppress("unused")
    fun onBackClicked() {
        popOnce(stack.value.active.instance::class)
    }

    protected fun popOnce(
        child: KClass<out Child>
    ) {
        if (child.isInstance(stack.active.instance)) {
            nav.pop()
        }
    }
}