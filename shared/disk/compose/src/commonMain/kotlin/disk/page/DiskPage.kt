package disk.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import disk.components.page.DiskPageComponent
import disk.mvi.DiskPageStore

@Composable
internal fun DiskPage(
    component: DiskPageComponent,
    authWidget: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(component) {
        component.labels.collect { label ->
            when (label) {
                is DiskPageStore.Label.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = label.message,
                        withDismissAction = true,
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->

        Box(
            modifier = Modifier.padding(padding)
        ) {
            authWidget()
            // контент страницы
        }
    }
}