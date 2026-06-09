package disk.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import disk.components.page.DiskPageComponent
import disk.mvi.DiskPageStore
import disk.page.PageConsts.BottomShadowHeight
import disk.page.PageConsts.HeaderHeight
import disk.page.PageConsts.HeaderShadowHeight
import utils.compose.consts.Paddings


@Composable
internal fun DiskPage(
    component: DiskPageComponent,
    authWidget: @Composable () -> Unit,
) {
    val model by component.model.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val safePaddings = WindowInsets.safeDrawing.asPaddingValues()
    val topPadding = safePaddings.calculateTopPadding()
    val bottomPadding = safePaddings.calculateBottomPadding()

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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        PageGrid(
            topPadding = topPadding,
            bottomPadding = bottomPadding,
            authWidget = authWidget,
            items = model.items
        )

        TopShadow(
            modifier = Modifier
                .align(Alignment.TopCenter),
            height = HeaderHeight / 2 + HeaderShadowHeight,
            paddingHeight = topPadding
        )


        BottomShadow(
            modifier = Modifier.align(Alignment.BottomCenter),
            height = BottomShadowHeight + bottomPadding
        )

        PageHeader(
            path = model.currentPath.value,
            modifier = Modifier.padding(top = topPadding).align(Alignment.TopCenter),
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(Paddings.medium),
        )
    }
}
