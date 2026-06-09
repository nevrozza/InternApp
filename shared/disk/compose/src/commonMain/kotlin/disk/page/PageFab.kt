package disk.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import disk.components.page.DiskPageComponent
import utils.compose.consts.Paddings
import utils.compose.icons.add
import utils.compose.icons.create_new_folder
import utils.compose.icons.create_note
import utils.compose.icons.upload_file

private val FabBottomPadding = 34.dp
private val FabSize = 56.dp
private val ExpandedFabShape = RoundedCornerShape(24.dp)
private val ActionTileShape = RoundedCornerShape(16.dp)
private val ActionLabelHeight = 40.dp
private val FabEnterExitSpec = tween<Float>(
    durationMillis = 360,
    easing = FastOutSlowInEasing,
)

@Composable
internal fun PageFab(
    component: DiskPageComponent,
    isExpanded: Boolean,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = Paddings.medium,
                end = Paddings.medium,
                bottom = bottomPadding + FabBottomPadding,
            ),
        contentAlignment = Alignment.BottomEnd,
    ) {
        AnimatedVisibility(
            modifier = Modifier.widthIn(max = 500.dp),
            visible = isExpanded,
            enter = slideInVertically(initialOffsetY = { it * 2 }),
            exit = slideOutVertically(targetOffsetY = { it * 2 }),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = ExpandedFabShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 6.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Paddings.small),
                    horizontalArrangement = Arrangement.spacedBy(Paddings.small),
                ) {
                    CreateActionTile(
                        icon = create_new_folder,
                        label = "Новая папка",
                        modifier = Modifier.weight(1f),
                        onClick = component::onCreateFolderClicked,
                    )
                    CreateActionTile(
                        icon = create_note,
                        label = "Создать .txt",
                        modifier = Modifier.weight(1f),
                        onClick = component::onCreateTextFileClicked,
                    )
                    CreateActionTile(
                        icon = upload_file,
                        label = "Загрузить файл",
                        modifier = Modifier.weight(1f),
                        onClick = component::onUploadFileClicked,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(FabEnterExitSpec) +
                    scaleIn(
                        animationSpec = FabEnterExitSpec,
                        initialScale = 0.7f,
                        transformOrigin = TransformOrigin(1f, 1f),
                    ),
            exit = fadeOut(FabEnterExitSpec) +
                    scaleOut(
                        animationSpec = FabEnterExitSpec,
                        targetScale = 0.7f,
                        transformOrigin = TransformOrigin(1f, 1f),
                    ),
        ) {
            Surface(
                modifier = Modifier.size(FabSize),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                tonalElevation = 10.dp,
            ) {
                Box(
                    modifier = Modifier
                        .size(FabSize)
                        .clickable(onClick = component::onCreateMenuClicked),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = add,
                        contentDescription = "Add",
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateActionTile(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(ActionTileShape)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = Paddings.small,
                top = Paddings.big,
                end = Paddings.small,
                bottom = Paddings.small,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .height(ActionLabelHeight),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
