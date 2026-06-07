package utils.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import utils.presentation.components.internal.asValue

abstract class DefaultMVIComponent<Intent : Any, State : Any, Label : Any>(
    componentContext: ComponentContext,
    storeFactory: () -> Store<Intent, State, Label>
) : ComponentContext by componentContext {

    protected val store =
        instanceKeeper.getStore(storeFactory)


    @OptIn(ExperimentalCoroutinesApi::class)
    val model: Value<State> by lazy {
        store.stateFlow.asValue(this.lifecycle)
    }

    val labels: Flow<Label> = store.labels

    fun onEvent(event: Intent) {
        store.accept(event)
    }
}