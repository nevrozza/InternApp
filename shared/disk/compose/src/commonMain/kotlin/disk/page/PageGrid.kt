package disk.page

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import disk.models.resources.BinaryFileResource
import disk.models.resources.DirectoryResource
import disk.models.resources.DiskResource
import disk.models.resources.TextFileResource
import disk.page.PageConsts.BottomShadowHeight
import disk.page.PageConsts.GridHorizontalPadding
import disk.page.PageConsts.GridItemMinSize
import disk.page.PageConsts.GridItemSpacing
import disk.page.PageConsts.HeaderHeight
import utils.compose.consts.Paddings
import utils.compose.icons.file
import utils.compose.icons.folder
import utils.compose.icons.note


@Composable
internal fun PageGrid(
    topPadding: Dp,
    bottomPadding: Dp,
    authWidget: @Composable () -> Unit,
    items: List<DiskResource>,
    menuTarget: DiskResource?,
    onItemClicked: (DiskResource) -> Unit,
    onItemMenuRequested: (DiskResource) -> Unit,
    onItemMenuDismissed: () -> Unit,
    onRenameClicked: (DiskResource) -> Unit,
    onDeleteClicked: (DiskResource) -> Unit,
    onEditTextClicked: (TextFileResource) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(GridItemMinSize),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = GridHorizontalPadding,
            top = HeaderHeight + topPadding + Paddings.semiMedium,
            end = GridHorizontalPadding,
            bottom = BottomShadowHeight + bottomPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(GridItemSpacing),
        verticalArrangement = Arrangement.spacedBy(GridItemSpacing),
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) },
            key = "AuthWidget"
        ) {
            Column {
                authWidget()
                Spacer(Modifier.height(Paddings.medium))
            }
        }

        items(
            items = items,
            key = DiskResource::localId,
        ) { resource ->
            DiskGridItem(
                resource = resource,
                isMenuExpanded = menuTarget?.localId == resource.localId,
                modifier = Modifier.animateItem(),
                onClick = { onItemClicked(resource) },
                onMenuRequested = { onItemMenuRequested(resource) },
                onMenuDismissed = onItemMenuDismissed,
                onRenameClicked = { onRenameClicked(resource) },
                onDeleteClicked = { onDeleteClicked(resource) },
                onEditTextClicked = {
                    if (resource is TextFileResource) {
                        onEditTextClicked(resource)
                    }
                },
            )
        }
    }
}

@Composable
private fun DiskGridItem(
    resource: DiskResource,
    isMenuExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onMenuRequested: () -> Unit,
    onMenuDismissed: () -> Unit,
    onRenameClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onEditTextClicked: () -> Unit,
) {

    val animatedBackground by animateColorAsState(
        if (isMenuExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = .7f) else MaterialTheme.colorScheme.surfaceContainer
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
            .pointerInput(resource.localId) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                            onMenuRequested()
                            event.changes.forEach { it.consume() }
                        }
                    }
                }
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = onMenuRequested,
                onLongClickLabel = "Открыть меню",
            )
            .background(animatedBackground)
            .padding(horizontal = Paddings.small).padding(top = Paddings.medium),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Paddings.small),
        ) {
            Icon(
                imageVector = when (resource) {
                    is DirectoryResource -> folder
                    is TextFileResource -> note
                    is BinaryFileResource -> file
                },
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = resource.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        ResourceDropdownMenu(
            resource = resource,
            expanded = isMenuExpanded,
            onDismiss = onMenuDismissed,
            onRenameClicked = onRenameClicked,
            onDeleteClicked = onDeleteClicked,
            onEditTextClicked = onEditTextClicked,
        )
    }
}

@Composable
private fun ResourceDropdownMenu(
    resource: DiskResource,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onRenameClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onEditTextClicked: () -> Unit,
) {
    var isDeleteConfirmationVisible by remember(resource.localId) { mutableStateOf(false) }

    LaunchedEffect(expanded, resource.localId) {
        if (!expanded) {
            isDeleteConfirmationVisible = false
        }
    }

    DisableSelection {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.animateContentSize()
        ) {

            if (isDeleteConfirmationVisible) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Точно удалить?",
                            color = MaterialTheme.colorScheme.error,
                        )
                    },
                    onClick = onDeleteClicked,
                )
                DropdownMenuItem(
                    text = { Text("Отмена") },
                    onClick = { isDeleteConfirmationVisible = false },
                )
            } else {

                if (resource is TextFileResource) {
                    DropdownMenuItem(
                        text = { Text("Редактировать") },
                        onClick = onEditTextClicked,
                    )
                }
                DropdownMenuItem(
                    text = { Text("Переименовать") },
                    onClick = onRenameClicked,
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Удалить",
                            color = MaterialTheme.colorScheme.error,
                        )
                    },
                    onClick = { isDeleteConfirmationVisible = true },
                )
            }
        }
    }
}
