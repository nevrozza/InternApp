package disk.page

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
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
import androidx.compose.ui.unit.dp
import disk.models.sync.SyncOperation
import disk.models.sync.SyncOperationState
import disk.models.sync.SyncOperationType
import disk.mvi.DiskPageStore.SyncIndicatorState
import utils.compose.icons.refresh
import utils.compose.icons.rounded_trash
import utils.compose.icons.warning

@Composable
internal fun SyncStatusButton(
    state: SyncIndicatorState,
    onForceSyncClick: () -> Unit,
    onCancelLocalSyncClick: (SyncOperation) -> Unit,
    operations: List<SyncOperation>
) {
    var expanded by remember { mutableStateOf(false) }

    if (state is SyncIndicatorState.Hidden) return

    Box {
        IconButton(
            onClick = {
                expanded = true
            }
        ) {
            when (state) {
                SyncIndicatorState.Syncing -> {
                    LoadingIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }

                SyncIndicatorState.Failed -> {
                    Icon(
                        warning,
                        contentDescription = null,
                    )
                }

                SyncIndicatorState.SyncingWithErrors -> {
                    Box {
                        LoadingIndicator(
                            modifier = Modifier.size(24.dp)
                        )

                        Icon(
                            warning,
                            contentDescription = null,
                            modifier = Modifier
                                .size(12.dp)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                SyncIndicatorState.Hidden -> Unit
            }
        }

        SyncOperationsMenu(
            expanded = expanded,
            onDismiss = { expanded = false },
            onForceSyncClick = onForceSyncClick,
            onCancelLocalSyncClick = onCancelLocalSyncClick,
            operations = operations
        )
    }
}

@Composable
private fun SyncOperationsMenu(
    expanded: Boolean,
    operations: List<SyncOperation>,
    onDismiss: () -> Unit,
    onForceSyncClick: () -> Unit,
    onCancelLocalSyncClick: (SyncOperation) -> Unit,
) {
    var cancelConfirmationOperation by remember { mutableStateOf<SyncOperation?>(null) }

    LaunchedEffect(expanded) {
        if (!expanded) {
            cancelConfirmationOperation = null
        }
    }

    DisableSelection {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.animateContentSize(),
        ) {
            DropdownMenuItem(
                text = { Text("Синхронизировать") },
                leadingIcon = {
                    Icon(
                        imageVector = refresh,
                        contentDescription = null,
                    )
                },
                onClick = onForceSyncClick,
            )

            if (operations.isNotEmpty()) {
                HorizontalDivider()

                val confirmationOperation = cancelConfirmationOperation

                if (confirmationOperation != null) {
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = "Удалить локальную копию?",
                                    color = MaterialTheme.colorScheme.error,
                                )

                                Text(
                                    text = confirmationOperation.path.value,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        },
                        onClick = {
                            onCancelLocalSyncClick(confirmationOperation)
                            cancelConfirmationOperation = null
                            onDismiss()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Отмена") },
                        onClick = { cancelConfirmationOperation = null },
                    )
                } else {
                    operations.forEachIndexed { index, operation ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = operation.path.value,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )

                                    Text(
                                        text = "${operation.operation.displayName()} • ${operation.state.displayName()}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            },
                            onClick = {},
                        )

                        if (operation.state != SyncOperationState.RUNNING) {
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = "Удалить локальную копию",
                                            color = MaterialTheme.colorScheme.error,
                                        )

                                        Text(
                                            text = "и отменить эту синхронизацию",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.error,
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = rounded_trash,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                },
                                onClick = { cancelConfirmationOperation = operation },
                            )
                        }

                        if (index != operations.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

private fun SyncOperationType.displayName(): String =
    when (this) {
        SyncOperationType.CREATE_FOLDER -> "MKDIR"
        SyncOperationType.DELETE -> "DEL"
        SyncOperationType.RENAME -> "RENAME"
        SyncOperationType.UPSERT_TEXT_FILE -> "UPSERT TEXT"
        SyncOperationType.UPLOAD_FILE -> "UPLOAD FILE"
    }

private fun SyncOperationState.displayName(): String =
    when (this) {
        SyncOperationState.PENDING -> "PENDING"
        SyncOperationState.RUNNING -> "RUNNING"
        SyncOperationState.FAILED -> "FAILED"
    }
