package disk.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import disk.components.flow.DiskComponent.Companion.RootPath
import disk.models.sync.SyncOperation
import disk.mvi.DiskPageStore.SyncIndicatorState
import disk.page.PageConsts.HeaderHeight
import utils.compose.consts.Paddings
import utils.compose.icons.back_button
import utils.compose.icons.refresh

private val ExpandedHeaderMinWidth = 600.dp

@Composable
internal fun PageHeader(
    path: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onBackClick: () -> Unit,

    syncState: SyncIndicatorState,
    onForceSyncClick: () -> Unit,
    onCancelLocalSyncClick: (SyncOperation) -> Unit,
    syncOperations: List<SyncOperation>,

    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(HeaderHeight)
            .padding(horizontal = Paddings.medium),
    ) {
        val showRefreshButton = maxWidth >= ExpandedHeaderMinWidth

        Row(
            modifier = Modifier.matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (
                path != RootPath.value
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(end = Paddings.ultraUltraSmall)
                ) {
                    Icon(
                        back_button,
                        contentDescription = "BackButton",
                        modifier = Modifier.offset(x = (-2).dp)
                    )
                }
            }

            Text(
                text = path,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (showRefreshButton) {
                IconButton(
                    onClick = onRefresh,
                    enabled = !isRefreshing,
                ) {
                    Icon(
                        imageVector = refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            SyncStatusButton(
                state = syncState,
                onForceSyncClick = onForceSyncClick,
                onCancelLocalSyncClick = onCancelLocalSyncClick,
                operations = syncOperations
            )
        }
    }
}

@Composable
internal fun TopShadow(
    modifier: Modifier = Modifier,
    height: Dp,
    paddingHeight: Dp,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Column {
        Box(Modifier.fillMaxWidth().height(paddingHeight).background(backgroundColor))
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor,
                            Color.Transparent,
                        )
                    )
                )
        )
    }
}

@Composable
internal fun BottomShadow(
    modifier: Modifier = Modifier,
    height: Dp,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background,
                    )
                )
            )
    )
}
