package auth.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import utils.compose.consts.Paddings


@Composable
internal fun UnauthorizedRow(
    errorMsg: String? = null,
    onLoginClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        errorMsg?.let { error ->
            Text("Ошибка: $error", modifier = Modifier.padding(bottom = Paddings.small))
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Войти через")
            Spacer(Modifier.width(Paddings.small))
            Button(
                onClick = onLoginClick
            ) {
                Text("Яндекс OAuth")
            }
        }
    }
}
