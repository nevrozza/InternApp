package disk.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import disk.components.page.DiskPageComponent
import disk.dialogs.CreateFolderDialogComponent
import disk.dialogs.CreateTextFileDialogComponent
import disk.dialogs.DiskDialogChild
import disk.dialogs.EditTextFileDialogComponent
import disk.dialogs.RenameResourceDialogComponent
import disk.validation.DiskNameValidationResult
import utils.compose.consts.Paddings

@Composable
internal fun DiskDialogs(component: DiskPageComponent) {
    val slot by component.dialogSlot.subscribeAsState()

    when (val child = slot.child?.instance) {
        is DiskDialogChild.CreateFolder -> CreateFolderDialog(child.component)
        is DiskDialogChild.CreateTextFile -> CreateTextFileDialog(child.component)
        is DiskDialogChild.RenameResource -> RenameResourceDialog(child.component)
        is DiskDialogChild.EditTextFile -> EditTextFileDialog(child.component)
        null -> Unit
    }
}

@Composable
private fun CreateFolderDialog(component: CreateFolderDialogComponent) {
    NameInputDialog(
        title = "Создать папку",
        confirmText = "Создать",
        initialName = "",
        validateName = component::validateName,
        onDismiss = component::onDismiss,
        onConfirm = component::onConfirm,
    )
}

@Composable
private fun CreateTextFileDialog(component: CreateTextFileDialogComponent) {
    var content by rememberSaveable { mutableStateOf("") }

    NameInputDialog(
        title = "Создать текстовый файл",
        confirmText = "Создать",
        initialName = "",
        validateName = component::validateName,
        onDismiss = component::onDismiss,
        onConfirm = { name ->
            component.onConfirm(name, content)
        },
        extraContent = {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                label = { Text("Текст") },
                minLines = 4,
            )
        },
    )
}

@Composable
private fun RenameResourceDialog(component: RenameResourceDialogComponent) {
    NameInputDialog(
        title = "Переименовать",
        confirmText = "Сохранить",
        initialName = component.currentName,
        validateName = component::validateName,
        onDismiss = component::onDismiss,
        onConfirm = component::onConfirm,
    )
}

@Composable
private fun EditTextFileDialog(component: EditTextFileDialogComponent) {
    var content by rememberSaveable(component.name) { mutableStateOf(component.initialContent) }

    AlertDialog(
        onDismissRequest = component::onDismiss,
        title = {
            Text(
                text = component.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
                label = { Text("Текст") },
                minLines = 8,
            )
        },
        confirmButton = {
            Button(
                onClick = { component.onConfirm(content) },
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = component::onDismiss) {
                Text("Отмена")
            }
        },
    )
}

@Composable
private fun NameInputDialog(
    title: String,
    confirmText: String,
    initialName: String,
    validateName: (String) -> DiskNameValidationResult,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    extraContent: @Composable (() -> Unit)? = null,
) {
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }
    val validation = validateName(name)
    val errorText = validation.errorText()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Название") },
                    isError = errorText != null,
                    supportingText = {
                        Text(
                            text = errorText.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                )

                if (extraContent != null) {
                    Column(
                        modifier = Modifier.padding(top = Paddings.semiMedium)
                    ) {
                        extraContent()
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = validation is DiskNameValidationResult.Valid,
                onClick = { onConfirm(name) },
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
    )
}

private fun DiskNameValidationResult.errorText(): String? {
    return when (this) {
        is DiskNameValidationResult.Valid -> null
        DiskNameValidationResult.Invalid.AlreadyExists -> "Файл или папка с таким названием уже есть"
        DiskNameValidationResult.Invalid.ContainsSlash -> "Название не должно содержать /"
        DiskNameValidationResult.Invalid.Empty -> "Введите название"
    }
}
