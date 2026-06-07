package root

import com.arkivanov.decompose.ComponentContext
import utils.presentation.navigation.DefaultStack

class RealRootComponent(
    componentContext: ComponentContext
) : RootComponent, DefaultStack<Root.Config, Root.Child>(
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
                ""
            )

            Root.Config.Settings -> Root.Child.Settings(
                ""
            )
        }

}